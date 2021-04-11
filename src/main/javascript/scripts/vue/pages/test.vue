<template>
    <div class="row">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    Test API Requests
                </div>
                <div class="card-body">
                    <div class="form-group">
                        <label for="template">Template:</label>
                        <select class="form-control" id="template" v-model="template"
                                @change="fillFromTemplate($event.target.value)">
                            <option :value="tplIndex" v-for="(tpl, tplIndex) in templates">
                                {{ tpl.name }}
                            </option>
                        </select>
                    </div>
                    <hr>
                    <div class="form-group">
                        <label for="method">Method:</label>
                        <select class="form-control" id="method" v-model="method">
                            <option :value="methodGet">GET</option>
                            <option :value="methodPost">POST</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="resource">Resource:</label>
                        <input type="text" class="form-control" placeholder="Like: [user, section, ...]"
                               id="resource" v-model="resource">
                    </div>
                    <div class="form-group">
                        <label for="action">Action:</label>
                        <input type="text" class="form-control" placeholder="Action: [list, get, remove, ...]"
                               id="action" v-model="action">
                    </div>
                    <div class="form-group">
                        <label for="extra">Extra:</label>
                        <input type="text" class="form-control" placeholder="Dynamic API request URL part"
                               id="extra" v-model="extra">
                        <small class="small text-secondary">
                            For example, it could be "1" if we want to make request like:<br>
                            GetRequest("user", "get", "1")
                        </small>
                    </div>
                    <div class="form-group">
                        <label for="method">Content Type:</label>
                        <select class="form-control" id="content-type" v-model="contentType">
                            <option :value="contentTypeJson">JSON</option>
                            <option :value="contentTypeFormData">Form data</option>
                            <option :value="contentTypeUrlencoded">Encoded URL</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="data">Data:</label>
                        <textarea class="form-control" rows="5" id="data"
                                  placeholder="{ ... }" v-model="data"></textarea>
                        <small class="small text-secondary">
                            Data in JSON or Form Data representation
                        </small>
                    </div>
                </div>
                <div class="card-footer">
                    <button type="button" class="btn btn-outline-primary btn-block" @click="send">
                        <feather type="send" :size="14"></feather>
                        Send request
                    </button>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="form-group">
                <template v-if="state === 'no'">
                    Perform any request to see some results.
                </template>
                <template v-if="state !== 'no'">
                    <textarea class="form-control" rows="34"
                              :class="{
                                  'border-success': state === 'success',
                                  'border-danger': state === 'failure'
                              }"
                              v-model="requestResult"
                              disabled
                    ></textarea>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
    import {GetRequest, PostRequest} from "@/scripts/api/request";
    import {ConfigBuilder} from "@/scripts/api/config";
    import {getPredefinedSectionId} from "@/scripts/functions/dev";
    import {
        CONTENT_TYPE_FORM_DATA,
        CONTENT_TYPE_JSON,
        CONTENT_TYPE_URLENCODED,
        METHOD_GET,
        METHOD_POST
    } from "@/scripts/api/constants";

    export default {
        data() {
            return {
                template: "no",
                templates: null,
                method: METHOD_GET,
                resource: "",
                action: "",
                extra: "",
                contentType: "json",
                data: "",
                state: "no",
                result: ""
            }
        },
        computed: {
            requestResult() {
                if (this.state === "loading") {
                    return "Please, wait ...";
                }
                if (this.state === "failure") {
                    return "Failed! :(";
                }
                return this.result;
            },
            methodGet() {
                return METHOD_GET;
            },
            methodPost() {
                return METHOD_POST;
            },
            contentTypeJson() {
                return CONTENT_TYPE_JSON;
            },
            contentTypeFormData() {
                return CONTENT_TYPE_FORM_DATA;
            },
            contentTypeUrlencoded() {
                return CONTENT_TYPE_URLENCODED;
            }
        },
        methods: {
            send() {
                let request;
                if (this.method === METHOD_GET) {
                    request = new GetRequest(this.resource, this.action, this.extra);
                } else {
                    request = new PostRequest(this.resource, this.action, this.extra);
                }
                this.state = "loading";
                this.result = "";
                let data;
                let configBuilder = new ConfigBuilder();
                configBuilder.contentType(this.contentType);
                if (this.contentType === this.contentTypeJson) {
                    data = this.data !== null && this.data.length > 0 ? JSON.parse(this.data) : null;
                } else {
                    data = this.data ? this.data : "";
                }
                request.setConfig(configBuilder.build());
                request.perform(data)
                    .then((data) => {
                        this.result = JSON.stringify(data, null, 2);
                        this.state = "success";
                    })
                    .catch((error) => {
                        this.result = JSON.stringify(error, null, 2);
                        this.state = "failure";
                    });
            },
            fillFromTemplate(tplIndex) {
                let tpl = this.templates[tplIndex];
                this.method = tpl.method;
                this.resource = tpl.resource;
                this.action = tpl.action;
                this.extra = tpl.extra;
                this.contentType = tpl.contentType;
                this.data = tpl.data;
            }
        },
        mounted() {
            let predefinedUserId = "0a000a00-0000-0a0a-aa00-a00a00aaaaa0";
            let predefinedSectionId = getPredefinedSectionId();
            let predefinedEntryId = "0c000c00-0000-0c0c-cc00-c00c00ccccc0";
            this.templates = {
                "no": {
                    name: "--- Not selected ---",
                    method: METHOD_GET,
                    resource: "",
                    action: "",
                    extra: "",
                    contentType: CONTENT_TYPE_JSON,
                    data: "",
                },
                "user-current": {
                    name: "Get current user",
                    method: METHOD_GET,
                    resource: "user",
                    action: "current",
                    extra: "",
                    contentType: CONTENT_TYPE_JSON,
                    data: "",
                },
                "user-get": {
                    name: "Get user by UUID",
                    method: METHOD_GET,
                    resource: "user",
                    action: "get",
                    extra: predefinedUserId,
                    contentType: CONTENT_TYPE_JSON,
                    data: "",
                },
                "section-tree": {
                    name: "Get sections tree",
                    method: METHOD_GET,
                    resource: "section",
                    action: "tree",
                    extra: "",
                    contentType: CONTENT_TYPE_JSON,
                    data: "",
                },
                "section-list": {
                    name: "List sections by UUIDs",
                    method: METHOD_POST,
                    resource: "section",
                    action: "list",
                    extra: "",
                    contentType: CONTENT_TYPE_JSON,
                    data: `[ "${predefinedSectionId}" ]`,
                },
                "translate-text": {
                    name: "Translate text",
                    method: METHOD_POST,
                    resource: "translate",
                    action: "text",
                    extra: "",
                    contentType: CONTENT_TYPE_URLENCODED,
                    data: "input-locale=ru-RU&output-locale=en-US&text=Здрасти, мордасти",
                },
                "speech-text": {
                    name: "Speech text",
                    method: METHOD_POST,
                    resource: "speech",
                    action: "text",
                    extra: "",
                    contentType: CONTENT_TYPE_URLENCODED,
                    data: "locale=ru-RU&text=Hello world!",
                },
                "download-file": {
                    name: "Download file",
                    method: METHOD_GET,
                    resource: "download",
                    action: "file",
                    extra: "__FILENAME__",
                    contentType: CONTENT_TYPE_URLENCODED,
                    data: "",
                }
            };
        }
    }
</script>

<style scoped>

</style>
