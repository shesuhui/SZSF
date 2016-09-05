__author__='sherwin.she'
#
_DEBUG=False
_PRINT=True

import  tools.utils as utils

if _DEBUG==True:
	import pdb
	pdb.set_trace()

 # search sourceTree,handle  one by one 
def  searchSourceTree(db_conn,ausp_conn,parent_id):
	sql=None
	if _PRINT==True:
		print(parent_id)
	sql="select id,orgname,parentid,parentname,type from t_orginfo where parentid='"+str(parent_id)+"'  order by parentid,priority"  
   	cur=None
   	if _PRINT==True:
   		print(sql)
        try:
               cur=db_conn.cursor()
               cur.execute(sql)
               data=cur.fetchall()
               seq=1
               for d in data:
               	        doMigration(ausp_conn,d,parent_id,seq)
               		searchSourceTree(db_conn,ausp_conn,str(d[0]))               		
               		seq+=1
        except Exception,e:
        	print e
            	raise e
        finally:
            	cur.close() 

def doMigration(ausp_conn,orgData,parent_id,seq):
	cur=None
	try:
		insertSql='insert into ausp_org_unit(c_id,c_name,c_short_name,c_parent_id,c_code,c_type,c_state,c_seq,\
                                     c_duty_id,c_comp_id,c_remark,c_creator_id,c_create_time,c_modifier_id,c_modify_time,c_sync)\
                                      values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)'
		cur=ausp_conn.cursor()		
		ausp_parent_id=None
		ausp_parent_code=None
	        if parent_id !=0:
	        	data=getOrgMappingInfo(ausp_conn,parent_id)
	        	ausp_parent_id=data[0]
	        	ausp_parent_code=data[1]
	        new_org_id=utils.getNewID()
	        org_code=(ausp_parent_code if not ausp_parent_code is None else '' )+format(seq,'04d')
	        print(org_code)
	        #print(orgData)
	        cur.execute("insert into orgMapping (principal_id,org_id,code) values("+str(orgData[0])+",'"+new_org_id+"','"+org_code+"')")
	        newData=(new_org_id,orgData[1],orgData[1],ausp_parent_id,org_code,'1','1',seq,None,None,None,None,utils.now(),None,utils.now(),'1')		      
	        if _PRINT==True:
	        	print(newData)
	        cur.execute(insertSql,newData)
	        if _PRINT==True:
	        	print('insert '+orgData[1]+' now')
		# ausp_conn.commit()
	except Exception, e:
		print e
		raise e
	finally:
		if not cur is None:
			cur.close()

def getOrgMappingInfo(ausp_conn,apsorgid):
        cur=None
	try:
	        sql='select org_id ,code from  orgMapping where principal_id='+apsorgid
		cur=ausp_conn.cursor()
		cur.execute(sql)
		data=cur.fetchone()
		return data
	except Exception, e:
		print e
		raise e
	finally:
		if not cur is None:
			cur.close()	


def  orgMigration(aps_conn,ausp_conn):
	try:
		ausp_conn.cursor().execute("delete from orgmapping")
	      	rootNode_id=0
        	searchSourceTree(aps_conn,ausp_conn,rootNode_id)
        except Exception,e:
            	raise e
        finally:
        	pass


