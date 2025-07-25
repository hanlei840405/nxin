package com.nxin.framework.enums;

import java.util.Arrays;
import java.util.List;

public interface Constant {
    String DOT = ".";
    String ACTIVE = "1";
    String INACTIVE = "0";
    String LOCKED = "2";
    String JOB = "1";
    String TRANSFORM = "2";
    String TRANS_SUFFIX = "ktr";
    String JOB_SUFFIX = "kjb";
    String GRAPH_SUFFIX = "graph";
    String ENCODING_UTF_8 = "UTF-8";
    String PASSWORD_ENCRYPTED_PREFIX = "Encrypted ";
    String AUTHORITY_PROBATION = "PROBATION";
    String ETL_START_NAME = "Start";
    String ETL_PARALLEL = "parallel";
    String NUMBER = "Number";
    String BOOLEAN = "Boolean";
    String SHELL_MODE_DESIGN = "DESIGN";
    String SHELL_MODE_SAVE = "SAVE";
    String SHELL_MODE_RUN = "RUN";
    String STREAMING = "1";
    String BATCH = "0";
    String ENV_PRODUCTION = "production";
    String ENV_PUBLISH = "publish";
    String ENV_DEV = "dev";
    String[] ENV_BUCKET = new String[]{"production", "publish", "dev"};
    String SHELL_STORAGE_DIR = "shellStorageDir"; // 脚本文件在FTP服务器上的存放位置
    String VAR_ATTACHMENT_DIR = "attachmentDir"; // 步骤中有使用上传的文件的存放位置
    String VAR_DOWNLOAD_DIR = "downloadDir"; // 步骤中有使用下载的文件的存放位置
    String RESOURCE_CATEGORY_ROOT = "ROOT";
    String RESOURCE_CATEGORY_HOME = "HOME";
    String RESOURCE_CATEGORY_BASIC = "BASIC";
    String RESOURCE_CATEGORY_ETL = "ETL";
    String RESOURCE_CATEGORY_JOB = "JOB";
    String RESOURCE_CATEGORY_REPORT = "REPORT";
    String RESOURCE_CATEGORY_SYSTEM = "SYSTEM";
    String RESOURCE_CATEGORY_PROJECT = "PROJECT";
    String RESOURCE_CATEGORY_CHART = "CHART";
    String RESOURCE_CATEGORY_LAYOUT = "LAYOUT";
    String RESOURCE_CATEGORY_MODEL = "MODEL";
    String RESOURCE_CATEGORY_DATASOURCE = "DATASOURCE";
    String RESOURCE_CATEGORY_FTP = "FTP";
    String RESOURCE_CATEGORY_SYSTEM_MEMBER = "USER";
    String RESOURCE_LEVEL_ROOT = "0";
    String RESOURCE_LEVEL_SYSTEM_MENU = "1";
    String RESOURCE_LEVEL_BUSINESS = "2";
    String PRIVILEGE_READ = "R";
    String PRIVILEGE_READ_WRITE = "RW";
    List<String> STREAMING_STEP = Arrays.asList("KafkaConsumerInput", "RecordsFromStream");
    String[] TRIM_TYPE_CODE = new String[]{"none", "left", "right", "both"};
    String[] VALUE_TYPE_CODE = new String[]{"field", "variable"};
    String[] FIELD_TYPE_CODE = new String[]{"string", "number", "datetime", "boolean"};
    String[] SUCCESS_CONDITION_BOOLEAN_CODE = new String[]{"true", "false"};
    String[] SUCCESS_CONDITION_CODE = new String[]{"equal", "different", "contains", "notcontains", "startswith", "notstatwith", "endswith", "notendwith", "regexp", "inlist", "notinlist"};
    String OWNER_DESIGNER = "designer";
    String OWNER_TASK = "task";
    String TOPIC_DESIGNER_SHUTDOWN = "DESIGNER_SHUTDOWN";
    String TOPIC_TASK_SHUTDOWN = "TASK_SHUTDOWN";
    String TOPIC_TASK_SUCCESS = "TASK_SUCCESS";
    String TOPIC_TASK_FAILURE = "TASK_FAILURE";
    int EXCEPTION_NOT_FOUNT = 10001;
    int EXCEPTION_UNAUTHORIZED = 10002;
    int EXCEPTION_OWNER = 10003;
    int EXCEPTION_DISABLED = 10004;
    int EXCEPTION_BAD_CREDENTIALS = 10005;
    int EXCEPTION_DUPLICATED = 10006;
    int EXCEPTION_DATASOURCE_CONNECT = 10007;
    int EXCEPTION_SQL_GRAMMAR = 10008;
    int EXCEPTION_ETL_GRAMMAR = 10009;
    int EXCEPTION_DATA = 10010;
    int EXCEPTION_RECORDS_NOT_MATCH = 10011;
    int EXCEPTION_ADD_SCHEDULE = 10012;
    int EXCEPTION_FTP_CONNECTION = 10013;
    int EXCEPTION_REMOVE_SCHEDULE = 10014;
    int EXCEPTION_FORBIDDEN_REMOVE_SELF = 10015;
    int EXCEPTION_FILE_NOT_EXIST = 10016;
    int EXCEPTION_XML_PARSE = 10017;
    int EXCEPTION_PLUGIN_CONVERT = 10018;
    int EXCEPTION_PUBLISHED = 10019;
    String GENERIC = "Generic";
    String EXCEPTION_PLUGIN_CONVERT_NAME = "EXCEPTION_PLUGIN_CONVERT_NAME";
    String SHELL_INFO = "shellInfo";
    String FILE_SEPARATOR = "/";
    String SSH_PATH = "ssh";
    int ATTACHMENT_CATEGORY_DOWNLOAD = 0;
    int ATTACHMENT_CATEGORY_EXPORT = 1;
    String AUDIT_STATUS_APPLY = "0";
    String AUDIT_STATUS_PASS = "1";
    String AUDIT_STATUS_REJECT = "9";
    String WEB_SOCKET_DESTINATION_MESSAGE = "message";
    String ACTION_ADD = "A";
    String ACTION_DEL = "D";
}
