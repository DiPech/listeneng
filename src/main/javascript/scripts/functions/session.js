import Vue from "vue";

import store from "@/scripts/plugins/store";
import router from "@/scripts/plugins/router";
import apiRequest from "@/scripts/api/api-request";

export function checkSessionAndFetchCurrentUser() {
    apiRequest.checkSession()
        .then(() => store.commit("setAuthorized", true))
        .then(() => apiRequest.getCurrentUser())
        .then(data => {
            store.commit("setCurrentUser", data)
        })
        .catch(() => store.commit("setAuthorized", false));
}

export function preventGuests() {
    return new Promise((resolve) => {
        if (store.state.isAuthorized) {
            resolve();
            return;
        }
        router.push({name: "login"});
    });
}
