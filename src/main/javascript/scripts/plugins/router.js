import Vue from "vue";
import VueRouter from "vue-router";

import cTreePage from "@/scripts/vue/pages/tree";
import cListPage from "@/scripts/vue/pages/list";
import cCardsPage from "@/scripts/vue/pages/cards";
import cListenPage from "@/scripts/vue/pages/listen";
import cLoginPage from "@/scripts/vue/pages/login";
import cLogoutPage from "@/scripts/vue/pages/logout";
import cTestPage from "@/scripts/vue/pages/test";
import {isDev} from "@/scripts/functions/dev";

Vue.use(VueRouter);

let routes = [
    {path: "/", name: "index", redirect: {name: "tree"}},
    {path: "/tree", name: "tree", component: cTreePage},
    {path: "/list", name: "list", component: cListPage},
    {path: "/cards", name: "cards", component: cCardsPage},
    {path: "/listen", name: "listen", component: cListenPage},
    {path: "/login", name: "login", component: cLoginPage},
    {path: "/logout", name: "logout", component: cLogoutPage},
];

if (isDev()) {
    routes.push({path: "/test", name: "test", component: cTestPage});
}

const router = new VueRouter({routes});

export default router;
