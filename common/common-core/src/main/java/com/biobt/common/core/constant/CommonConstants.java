package com.biobt.common.core.constant;

/**
 * 通用常量
 */
public class CommonConstants {
    
    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;
    
    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;
    
    /**
     * 默认页码
     */
    public static final Integer DEFAULT_PAGE_NUM = 1;
    
    /**
     * 默认每页大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 最大每页大小
     */
    public static final Integer MAX_PAGE_SIZE = 1000;
    
    /**
     * 编码格式
     */
    public static final String UTF8 = "UTF-8";
    
    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * 日期时间格式
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 时间戳格式
     */
    public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    
    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";
    
    /**
     * 超级管理员角色
     */
    public static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";
    
    /**
     * 管理员角色
     */
    public static final String ADMIN_ROLE = "ADMIN";
    
    /**
     * 普通用户角色
     */
    public static final String USER_ROLE = "USER";
    
    /**
     * 系统用户
     */
    public static final String SYSTEM_USER = "system";
    
    /**
     * 匿名用户
     */
    public static final String ANONYMOUS_USER = "anonymous";
    
    /**
     * 删除标记 - 已删除
     */
    public static final Boolean DELETED = true;
    
    /**
     * 删除标记 - 未删除
     */
    public static final Boolean NOT_DELETED = false;
    
    /**
     * 状态 - 启用
     */
    public static final Integer STATUS_ENABLE = 1;
    
    /**
     * 状态 - 禁用
     */
    public static final Integer STATUS_DISABLE = 0;
    
    /**
     * 是否标记 - 是
     */
    public static final String YES = "Y";
    
    /**
     * 是否标记 - 否
     */
    public static final String NO = "N";
    
    /**
     * 性别 - 男
     */
    public static final String GENDER_MALE = "M";
    
    /**
     * 性别 - 女
     */
    public static final String GENDER_FEMALE = "F";
    
    /**
     * 性别 - 未知
     */
    public static final String GENDER_UNKNOWN = "U";
    
    /**
     * 顶级父节点ID
     */
    public static final Long TOP_PARENT_ID = 0L;
    
    /**
     * 树形结构根节点标识
     */
    public static final String TREE_ROOT = "0";
    
    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 5;
    
    /**
     * 密码最小长度
     */
    public static final Integer PASSWORD_MIN_LENGTH = 6;
    
    /**
     * 密码最大长度
     */
    public static final Integer PASSWORD_MAX_LENGTH = 20;
    
    /**
     * 用户名最小长度
     */
    public static final Integer USERNAME_MIN_LENGTH = 3;
    
    /**
     * 用户名最大长度
     */
    public static final Integer USERNAME_MAX_LENGTH = 20;
    
    /**
     * 邮箱正则表达式
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    /**
     * 手机号正则表达式
     */
    public static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    
    /**
     * 身份证号正则表达式
     */
    public static final String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    
    /**
     * IP地址正则表达式
     */
    public static final String IP_REGEX = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
    
    /**
     * 文件上传路径
     */
    public static final String UPLOAD_PATH = "/upload";
    
    /**
     * 临时文件路径
     */
    public static final String TEMP_PATH = "/temp";
    
    /**
     * 头像上传路径
     */
    public static final String AVATAR_PATH = "/avatar";
    
    /**
     * 文档上传路径
     */
    public static final String DOCUMENT_PATH = "/document";
    
    /**
     * 图片上传路径
     */
    public static final String IMAGE_PATH = "/image";
    
    /**
     * 允许上传的图片格式
     */
    public static final String[] IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
    
    /**
     * 允许上传的文档格式
     */
    public static final String[] DOCUMENT_EXTENSIONS = {"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt"};
    
    /**
     * 最大文件大小（字节）- 10MB
     */
    public static final Long MAX_FILE_SIZE = 10 * 1024 * 1024L;
    
    /**
     * 最大图片大小（字节）- 5MB
     */
    public static final Long MAX_IMAGE_SIZE = 5 * 1024 * 1024L;
}