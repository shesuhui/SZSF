# -*- coding:utf-8 -*-
__author__ = 'sherwin.she'

_DEBUG =True
_PRINT = True

from  tools import utils
import time
import datetime
import traceback

if _DEBUG==True:
	import pdb
	pdb.set_trace()


class UserMigration (object):

    """docstring for migrateuser """

    def __init__(self, aps_conn, ausp_conn):
        super(UserMigration, self).__init__()
        self.aps_conn = aps_conn
        self.ausp_conn = ausp_conn

    def doMigration(self):
        self.migrateUserMainInfo()
        # self.ausp_conn.commit()

    def migrateUserMainInfo(self):
        sql = "select id,account,name,password,orgid,priority,status,nickname,phone,fax,gender,\
        mobile from t_userinfo where account<>'admin' and orgid is not null"
        aps_cur = None
        ausp_cur = None
        try:
            aps_cur = self.aps_conn.cursor()
            data = aps_cur.execute(sql)
            ausp_cur = self.ausp_conn.cursor()
            insertAccountSql = "INSERT INTO ausp_org_user (  c_id,  c_emp_id,  c_emp_name,  c_code,  \
	       			c_user_name,  c_password,  c_org_unit_id,  c_type,  c_state,  c_strategy,  c_auth_mode,\
  				c_valid_time,  c_invalid_time,  c_creator_id,  c_create_time,  c_modifier_id,  c_modify_time,\
  				c_sync) VALUES  ( %s, %s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"

            insertEmpSql = "INSERT INTO ausp_org_emp (  c_id,  c_name,  c_org_unit_id,  c_sn,  c_job_number,\
				c_sex,  c_id_number,  c_native_place,  c_education,  c_school,  c_birthday,  c_contact_address,\
                    	 	c_oph_one,  c_oph_two,  c_mobile_one,  c_mobile_two,  c_fax,  c_personal_email,  c_enterprise_email,\
                     		c_administrative_level,  c_duty,  c_state,  c_photo,  c_remark,  c_creator_id,  c_create_time,\
                     		c_modifier_id,  c_modify_time) VALUES  (    %s,    %s,    %s,    %s,   %s,  %s,    %s,  %s,    %s,   %s,    %s,\
                     		%s,    %s,    %s,    %s,    %s,    %s,    %s,    %s,   %s,    %s,    %s,    %s,   %s,   %s,    %s,    %s,    %s  ) "

            validDay = datetime.date.today()
            invalidDay = datetime.date(
                datetime.date.today().year+20, datetime.date.today().month, datetime.date.today().day)
            # if user has no defaultOrgUnit then give a default one
            defaultOrgName = "停用帐号"
            ausp_cur.execute("select c_id from ausp_org_unit where c_name='"+defaultOrgName+"'")
            defaultOrgUnit=ausp_cur.fetchone()[0]

            for d in data:
                now = datetime.datetime.now()
                gender = None
                if 2 == d[10]:
                    gender = "M"
                else :
                    gender = "F"

                ausp_cur.execute("select org_id from orgmapping where principal_id='"+str(d[4])+"'")
                rowSet=ausp_cur.fetchone()
                if not rowSet is None:
                    defaultOrgUnit=rowSet[0]
                else:
                    continue
                # insert employee info******************
                newEmpID = utils.getNewID()
                insertData = (newEmpID, d[2], defaultOrgUnit, d[5], None, gender, None, None, None, None, None, None, d[8], None, d[11], None, d[9], None, None,
                              None, None, None, None, None, None, now, None, now)
                ausp_cur.execute(insertEmpSql, insertData)
                # insert Account Info*******************
                newAccountID = utils.getNewID()
                insertData = (newAccountID, newEmpID, d[2], d[1], d[
                              1], None, defaultOrgUnit, "10", 1, "10", "10", validDay, invalidDay, None, now, None, now, "1")
                ausp_cur.execute(insertAccountSql, insertData)
                if _PRINT == True:
                    print('insert account info  '+d[2]+' now')
                # grant role to user
                # self.grantRole2User(d)
        except Exception, e:
        	# self.ausp_conn.rollback()
                traceback.print_exc()
            	raise e
        finally:
            pass

    def grantRole2User(self, userInfo):
        user_name = userInfo[0]
        sql = "select attr_value from security_attribute a where a.attr_name='principal.label' and \
       		a.principal_id in(select to_principal_id from SECURITY_PRINCIPAL_ASSOC t  where  \
       		t.from_principal_id in (select principal_id from SECURITY_PRINCIPAL p where \
		p.principal_name='"+user_name+"' )) and a.principal_id in(select principal_id from \
		SECURITY_PRINCIPAL where  principal_type='role' )"
        aps_cur = self.aps_conn.cursor()
        ausp_cur=self.ausp_conn.cursor()
        if _PRINT==True:
        	print(sql)
        # get role info from aps
        data = aps_cur.execute(sql)
        list = []
        insertSql = "INSERT INTO ausp_permi_user_role (c_id,c_user_id,c_role_id,c_creator_id,\
		c_create_time,c_modifier_id,c_modify_time) VALUES(%s,%s,%s,%s,%s,%s,%s)"
        for d in data:
            sql = "SELECT c_id FROM ausp_org_role WHERE c_name='"+ d[0] +"'"
            ausp_cur.execute(sql)
            ausp_role_data = ausp_cur.fetchone()
            ausp_role_id=ausp_role_data[0]
            sql="SELECT c_id FROM ausp_org_user WHERE c_user_name='"+user_name+"'"
            ausp_cur.execute(sql)
            user_id=ausp_cur.fetchone()[0]
            insertData = (
                utils.getNewID(), user_id, ausp_role_id, None, utils.now(), None, utils.now())
            list.append(insertData)
        self.ausp_conn.cursor().executemany(insertSql, list)
