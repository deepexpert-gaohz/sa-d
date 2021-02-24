layui.define(function (exports) {

    //hash监听列表
    var url_hash_change_json = {};

    var json = {
        parse: function (url) {

            var url_arr = {
                host: "",
                parameter: [],
                hash: ""
            };
            var url = url ? url : window.location.href;
            if (url.substr(-1, 1) == "#") {
                url = url.substr(0, url.length - 1)
            }
            var host_split = url.split("?");
            url_arr.host = host_split[0];
            var parameter = host_split[1];
            if (parameter == undefined) {
                parameter = ""
            }
            var par_split = parameter.split("#");
            url_arr.hash = par_split[1];
            parameter = par_split[0];
            parameter = par_split[0];
            for (var i = 0; i < par_split.length; i++) {
                var sp = par_split[i].split("&");
                for (var j = 0; j < sp.length; j++) {
                    var par = sp[j].split("=");
                    if (par[0]) {
                        url_arr.parameter.push(par)
                    }
                }
            }
            return url_arr

        }
        //清除指定名称的参数
        , del: function (key, url) {
            var url = url ? url : window.location.href;
            var url_arr = this.parse(url);
            url = url_arr.host + "?";
            var parameter = url_arr.parameter;
            if (parameter.length > 0) {
                for (var i = 0; i < parameter.length; i++) {
                    var par = parameter[i];
                    if (par[0]) {
                        if (key != par[0]) {
                            url += par[0] + "=" + par[1] + "&"
                        }
                    }
                }
                if (url_arr.hash) {
                    url += "#" + url_arr.hash
                }
            }
            return url
        }
        //获取指定名称的参数
        , get: function (key, url) {

            var url_arr = this.parse(url);
            if (key == "#") {
                return url_arr.hash
            } else {
                var arr = url_arr.parameter;
                for (var i = 0; i < arr.length; i++) {
                    var sp = arr[i];
                    if (sp[0]) {
                        if (key == sp[0]) {
                            var reg=/%3C|%3E|<|>/g;
                            return sp[1].replace(reg, '');
                        }
                    }
                }
            }
            return "";
        }
        //添加一个参数  如果已存在则覆盖
        , add: function (key, val, url) {
            var url = url ? url : window.location.href;
            var url_arr = this.parse(url);
            url = url_arr.host + "?";
            var parameter = url_arr.parameter;
            var has = 0;
            if (parameter.length > 0) {
                for (var i = 0; i < parameter.length; i++) {
                    var par = parameter[i];
                    if (par[0]) {
                        if (key == par[0]) {
                            url += key + "=" + val + "&";
                            has = 1
                        } else {
                            url += par[0] + "=" + par[1] + "&"
                        }
                    }
                }
                if (has == 0) {
                    url += key + "=" + val
                } else {
                    url = url.substr(0, url.length - 1)
                }
                if (url_arr.hash) {
                    url += "#" + url_arr.hash
                }
            } else {
                url += key + "=" + val
            }
            return url
        }
        //获取hash值
        , get_hash: function (name, url) {

            var key_values = this.get_hash_key_value(url);
            for (var j in key_values) {
                if (j == name) {
                    return key_values[j];
                }
            }
            return "";
        }
        //删除指定名称的hash
        , del_hash: function (name, url) {

            var key_values = this.get_hash_key_value(url);
            var result = [];
            for (var j in key_values) {
                if (j != name) {
                    result.push(j + "=" + key_values[j]);
                }
            }
            //window.location.hash = result.join("&");
            var url = url ? url : window.location.href;
            return url.split('#')[0] + "#" + result.join("&");

        }
        //添加一个hash参数
        , add_hash: function (name, value, url) {

            var key_values = this.get_hash_key_value(url);
            var result = [];
            var ishas = false;

            for (var j in key_values) {
                if (j == name) {
                    result.push(name + "=" + value);
                    ishas = true;
                } else {
                    if (j.length > 0)
                        result.push(j + "=" + (key_values[j].length > 1 ? key_values[j] : ""));
                }
            }
            if (!ishas) {
                result.push(name + "=" + value);
            }
            // window.location.hash = result.join("&");
            var url = url ? url : window.location.href;
            return url.split('#')[0] + "#" + result.join("&");
        }
        //设置hash参数 与 add_hash 的差别为，这方法只会存在一个参数
        , set_hash: function (name, value, url) {

            var url = url ? url : window.location.href;
            return url.split('#')[0] + "#" + name + "=" + value;
        }
        //获取hash参数为json格式
        , get_hash_key_value: function (url) {

            var url = url ? url : window.location.href;
            var hash = url.split('#');

            if (!hash || hash.length <= 1) return [];

            hash = hash[1].split('&');

            var result = {};
            for (var i = 0; i < hash.length; i++) {

                var arr = hash[i].split('=');
                if (arr[0].length > 0) result[arr[0]] = (arr.length > 1 ? arr[1] : "");
            }

            return result;

        }
        //开始监听hash变化
        , hash_changed_listen: function () {

            var obj = this;

            $(window).bind('hashchange', function (eve) {

                var key_values = obj.get_hash_key_value();

                for (var j in key_values) {
                    if (url_hash_change_json.hasOwnProperty(j)) {

                        if (key_values[j] != url_hash_change_json[j]["value"]) {

                            var fns = url_hash_change_json[j]["fn"];
                            for (var i = 0; i < fns.length; i++) {
                                fns[i](key_values[j]);
                            }
                            url_hash_change_json[j]["value"] = key_values[j];
                        }
                    }
                }
            });
        }
        //监听指定名称的hash变化，fn 回调，当发生变化时回调
        , hash_changed_sub: function (name, fn) {

            if (url_hash_change_json.hasOwnProperty(name)) {
                url_hash_change_json[name]["fn"].push(fn);
            } else {
                url_hash_change_json[name] = { fn: [fn], value: this.get(name) };
            }
        }
    };

    exports('murl', json);
});