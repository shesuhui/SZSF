/**
 * 
 */
package com.apusic.szsf.sync.domain;

import com.apusic.ausp.AbstractEntity;

/**
 * 抽象同步用户
 */
@SuppressWarnings("serial")
public abstract class AbstractSyncUser extends AbstractEntity {
	
    /**
     * 用户ID
     */
    protected String id;
    /**
     * 账号编码
     */
    protected String code;
    /**
     * 账号名称
     */
    protected String userName;
    /**
     * 姓名
     */
    protected String empName;
    /**
     * 所属组织ID
     */
    protected String orgUnitId;
    /**
     * 用户类型
     */
    protected String type;
    /**
     * 用户状态
     */
    protected String state;
    /**
     * 顺序号
     */
    protected String sn;
    /**
     * 工号
     */
    protected String jobNumber;
    /**
     * 性别
     */
    protected String sex;
    /**
     * 身份证号码
     */
    protected String idNumber;
    /**
     * 籍贯
     */
    protected String nativePlace;
    /**
     * 文化程度
     */
    protected String education;
    /**
     * 毕业院校
     */
    protected String school;
    /**
     * 出生日期
     */
    protected String birthday;
    /**
     * 联系地址
     */
    protected String contactAddress;
    /**
     * 头像
     */
    protected String photo;

	/**
     * 
     */
    public AbstractSyncUser() {
        // TODO Auto-generated constructor stub
    }
	

    /**
     * 取得用户ID
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     * 设置用户ID
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 取得账号编码
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 设置账号编码
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 取得账号名称
     * @return
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * 设置账号名称
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 取得姓名
     * @return
     */
    public String getEmpName() {
        return this.empName;
    }

    /**
     * 设置姓名
     * @param empName
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    /**
     * 取得所属组织ID
     * @return
     */
    public String getOrgUnitId() {
        return this.orgUnitId;
    }

    /**
     * 设置所属组织ID
     * @param orgUnitId
     */
    public void setOrgUnitId(String orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    /**
     * 取得用户类型
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * 设置用户类型
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 取得用户状态
     * @return
     */
    public String getState() {
        return this.state;
    }

    /**
     * 设置用户状态
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 取得顺序号
     * @return
     */
    public String getSn() {
        return this.sn;
    }

    /**
     * 设置顺序号
     * @param sn
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 取得工号
     * @return
     */
    public String getJobNumber() {
        return this.jobNumber;
    }

    /**
     * 设置工号
     * @param jobNumber
     */
    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    /**
     * 取得性别
     * @return
     */
    public String getSex() {
        return this.sex;
    }

    /**
     * 设置性别
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 取得身份证号码
     * @return
     */
    public String getIdNumber() {
        return this.idNumber;
    }

    /**
     * 设置身份证号码
     * @param idNumber
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * 取得籍贯
     * @return
     */
    public String getNativePlace() {
        return this.nativePlace;
    }

    /**
     * 设置籍贯
     * @param nativePlace
     */
    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    /**
     * 取得文化程度
     * @return
     */
    public String getEducation() {
        return this.education;
    }

    /**
     * 设置文化程度
     * @param education
     */
    public void setEducation(String education) {
        this.education = education;
    }

    /**
     * 取得毕业院校
     * @return
     */
    public String getSchool() {
        return this.school;
    }

    /**
     * 设置毕业院校
     * @param school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * 取得出生日期
     * @return
     */
    public String getBirthday() {
        return this.birthday;
    }

    /**
     * 设置出生日期
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * 取得联系地址
     * @return
     */
    public String getContactAddress() {
        return this.contactAddress;
    }

    /**
     * 设置联系地址
     * @param contactAddress
     */
    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    /**
     * 取得头像
     * @return
     */
    public String getPhoto() {
        return this.photo;
    }

    /**
     * 设置头像
     * @param photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
}
