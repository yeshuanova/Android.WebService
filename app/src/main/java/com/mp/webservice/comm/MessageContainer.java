package com.mp.webservice.comm;

/**
 * Created by PeterLi on 2015/6/22.
 */
public class MessageContainer {

//    public static class BaseJSON {
//
//    }

    // ublms_app_api_login (send)
    public static class ApiLoginRequest {
        private String uid = "";
        private String pwd = "";

        public String get_api_uid() {
            return uid;
        }

        public void set_api_uid(String uid) {
            this.uid = uid;
        }

        public String get_api_pwd() {
            return pwd;
        }

        public void set_api_pwd(String pwd) {
            this.pwd = pwd;
        }

    }

    // ublms_app_api_login (get)
    public static class ApiLoginResponse {//} extends BaseJSON {
        private String sid = "";
        private String expire = "";

        public String get_sid() {
            return sid;
        }

        public void set_sid(String sid) {
            this.sid = sid;
        }

        public String get_sid_expire() {
            return expire;
        }

        public void set_sid_expire(String expire) {
            this.expire = expire;
        }

    }

    // ublms_app_auth_login (send)
    public static class AuthLoginRequest { //extends BaseJSON {
        private String account = "";
        private String password = "";
        private int type = 0;

        public String get_user_account() {
            return account;
        }

        public void set_user_name(String user) {
            this.account = user;
        }

        public String get_user_pwd() {
            return password;
        }

        public void set_user_pwd(String pwd) {
            this.password = pwd;
        }

        public int get_user_type() {
            return type;
        }

        public void set_user_type(int user_type) {
            this.type = user_type;
        }

    }

    // ublms_app_auth_login (get)
    public static class AuthLoginResponse { // extends BaseJSON {
        private String authid = "";
        private String auth_name = "Unknown";

        public String get_auth_id() {
            return authid;
        }

        public void set_auth_id(String auth_id) {
            this.authid = auth_id;
        }

        public String get_auth_name() {
            return auth_name;
        }

        public void set_auth_name(String auth_name) {
            this.auth_name = auth_name;
        }
    }

}
