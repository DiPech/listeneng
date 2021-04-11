import Vue from "vue";
import VueFeather from "vue-feather";
import VueToasted from "vue-toasted";
import VueTextareaAutosize from "vue-textarea-autosize";
import VTooltip from "v-tooltip";
import "bootstrap";

import cIndex from "@/scripts/vue/index";
import router from "@/scripts/plugins/router";
import store from "@/scripts/plugins/store";
import getOptions from "@/scripts/plugins/toasted";
import "@/styles/main";

Vue.use(VueFeather);
Vue.use(VTooltip);
Vue.use(VueToasted, getOptions());
Vue.use(VueTextareaAutosize);

new Vue({
    el: "#app",
    router,
    store,
    components: {
        cIndex,
    }
});
