import Vue from "vue";

import {ConfigBuilder} from "@/scripts/api/config";
import logger from "@/scripts/service/logger";
import axios from "@/scripts/plugins/axios";
import {
    CONTENT_TYPE_FORM_DATA,
    CONTENT_TYPE_JSON,
    CONTENT_TYPE_URLENCODED,
    METHOD_GET,
    METHOD_POST
} from "@/scripts/api/constants";

class Request {

    constructor(method, resource, action, extra) {
        this._method = method;
        this._resource = resource;
        this._action = action;
        this._extra = extra ? extra : "";
        this.setConfig(new ConfigBuilder().build());
    }

    setConfig(config) {
        this._config = config;
        return this;
    }

    perform(data) {
        if (typeof data === "undefined") {
            data = this._config.contentType === CONTENT_TYPE_JSON ? {} : "";
        }
        return new Promise((resolve, reject) => {
            let extra = this._extra ? "/" + this._extra : "";
            let url = `/api/${this._resource}/${this._action}${extra}`;
            logger.log(`[${this._method}] Requesting "${url}" with data:`, data);
            let contentType;
            if (this._config.contentType === CONTENT_TYPE_JSON) {
                contentType = "application/json;charset=UTF-8";
            } else if (this._config.contentType === CONTENT_TYPE_URLENCODED) {
                contentType = "application/x-www-form-urlencoded";
            } else if (this._config.contentType === CONTENT_TYPE_FORM_DATA) {
                contentType = "multipart/form-data";
            }
            let headers = {"Content-Type": contentType};
            axios.request({url, data, headers, method: this._method})
                .then((response) => {
                    if (response.data.status !== "OK") {
                        if (!this._config.isSilent) {
                            let message = response.data.message;
                            message = message.replace(/(?:\r\n|\r|\n)/g, '<br>');
                            Vue.toasted.error(message);
                        }
                        reject(response.data.message);
                    } else {
                        logger.log(`Response:`, response.data.data)
                        resolve(response.data.data);
                    }
                })
                .catch((error) => {
                    if (!this._config.isSilent) {
                        Vue.toasted.error(error.message);
                    }
                    reject(error.message);
                });
        });
    }

}

export class GetRequest extends Request {

    constructor(resource, action, extra) {
        super(METHOD_GET, resource, action, extra);
    }

}

export class PostRequest extends Request {

    constructor(resource, action, extra) {
        super(METHOD_POST, resource, action, extra);
    }

}
