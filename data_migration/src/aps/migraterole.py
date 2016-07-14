__author__='sherwin.she'
#
_DEBUG=False
_PRINT=True

import  tools.utils

if _DEBUG==True:
	import pdb
	pdb.set_trace()

class RoleMigration(object):
	"""docstring for RoleMigration"""
	def __init__(self, aps_conn,ausp_conn):
		super(RoleMigration, self).__init__()
		self.aps_conn=aps_conn
		self.ausp_conn=ausp_conn

	def doMigration(self):
		sql="select principal_id,attr_value from security_attribute a   where a.attr_name='principal.label' \
                and a.principal_id in(select principal_id from SECURITY_PRINCIPAL where  principal_type='role' )"
                aps_cur=None
                ausp_cur=None
                try:
                        if _DEBUG==True:
				pdb.set_trace()
                	aps_cur=self.aps_conn.cursor()
                	data=aps_cur.execute(sql)
                	ausp_cur=self.ausp_conn.cursor()
		        insertSql='insert ausp_org_role (c_id,c_code,c_name,c_state,c_remark,c_creator_id,c_create_time,c_modifier_id,c_modify_time)\
		        		    values(%s,%s,%s,%s,%s,%s,%s,%s,%s)'
		        seq=1
                	for d in data:
	                	newID=utils.getNewID()
	                	code=format(seq,'04d')
	                	insertData=(newID,code,d[1],1,None,None,None,None,None)
	                	ausp_cur.execute(insertSql,insertData)
	                	if _PRINT==True:
	                		print('insert '+d[1]+' now')
	                	seq+=1
		        # self.ausp_conn.commit()
                except Exception, e:
                	raise e
                finally:
                	#aps_cur.close()
                	#ausp_cur.close()
                	pass
