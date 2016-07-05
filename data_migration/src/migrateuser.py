# -*- coding:utf-8 -*-
__author__='sherwin.she'

_DEBUG=True
_PRINT=True

import utils
import time
import datetime

# if _DEBUG==True:
# 	import pdb
# 	pdb.set_trace()


class UserMigration (object):
	"""docstring for migrateuser """
	def __init__(self, aps_conn,ausp_conn):
		super(UserMigration , self).__init__()
		self.aps_conn=aps_conn
		self.ausp_conn=ausp_conn

	def doMigration(self):
		sql="select principal_name,principal_id,userpriority,username,userid,usergender,telephone, \
		usermobile,fullspelling,shortspelling from ausp_user where principal_name<>'admin' "
		aps_cur=None
		ausp_cur=None 
		try:
			aps_cur=self.aps_conn.cursor()
			data=aps_cur.execute(sql)
       			ausp_cur=self.ausp_conn.cursor()
	       		insertAccountSql="INSERT INTO ausp_org_user (  c_id,  c_emp_id,  c_emp_name,  c_code,  \
	       			c_user_name,  c_password,  c_org_unit_id,  c_type,  c_state,  c_strategy,  c_auth_mode,\
  				c_valid_time,  c_invalid_time,  c_creator_id,  c_create_time,  c_modifier_id,  c_modify_time,\
  				c_sync) VALUES  ( %s, %s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
                     
			insertEmpSql="INSERT INTO ausp_org_emp (  c_id,  c_name,  c_org_unit_id,  c_sn,  c_job_number,\
				c_sex,  c_id_number,  c_native_place,  c_education,  c_school,  c_birthday,  c_contact_address,\
                    	 	c_oph_one,  c_oph_two,  c_mobile_one,  c_mobile_two,  c_fax,  c_personal_email,  c_enterprise_email,\
                     		c_administrative_level,  c_duty,  c_state,  c_photo,  c_remark,  c_creator_id,  c_create_time,\
                     		c_modifier_id,  c_modify_time) VALUES  (    %s,    %s,    %s,    %s,   %s,  %s,    %s,  %s,    %s,   %s,    %s,\
                     		%s,    %s,    %s,    %s,    %s,    %s,    %s,    %s,   %s,    %s,    %s,    %s,   %s,   %s,    %s,    %s,    %s  ) "

		        validDay=datetime.date.today()
		        invalidDay=datetime.date(datetime.date.today().year+5,datetime.date.today().month,datetime.date.today().day)
		        defaultOrgUnit="a6b2c76240b811e69a68000c291ffe15"
		        for d in data:
				now=datetime.datetime.now()
	                	gender=None
	                	if "女"==d[5]:
	                		gender="F"
	                	elif "男"==d[5]:
	                		gender="M"

	                	# insert employee info******************
	                	newEmpID=utils.getNewID()
	                	insertData=(newEmpID,d[3],defaultOrgUnit,d[2],None,gender,None,None,None,None,None,None,d[6],None,d[7],None,None,None,None,
	                	None,None,None,None,None,None,now,None,now)
	                	ausp_cur.execute(insertEmpSql,insertData)
	                	# insert Account Info*******************
	                	newAccountID=utils.getNewID()
	                	insertData=(newAccountID,newEmpID,d[3],d[0],d[0],None,defaultOrgUnit,"10",1,"10","10",validDay,invalidDay,None,now,None,now,"0")
	                	ausp_cur.execute(insertAccountSql,insertData)
	                	if _PRINT==True:
	                		print('insert account info  '+d[3]+' now')
	                	if _PRINT==True:
	                		print('insert employee info  '+d[3]+' now')

		        self.ausp_conn.commit()
		except Exception, e:
	       		raise e
	       	finally:
	       		pass
			

		