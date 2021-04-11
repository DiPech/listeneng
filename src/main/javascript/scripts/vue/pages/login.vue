<template>
    <div>
        <div class="row">
            <div class="col-md-6 offset-md-3">
                <div class="card">
                    <div class="card-body">
                        <div class="input-group mb-4">
                            <div class="input-group-prepend">
                                <span class="input-group-text">
                                    <feather type="mail"></feather>
                                </span>
                            </div>
                            <input type="email" class="form-control" placeholder="example@mail.ru"
                                   v-model="email">
                        </div>
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text">
                                    <feather type="key"></feather>
                                </span>
                            </div>
                            <input type="password" class="form-control" placeholder="**************"
                                   v-model="password">
                        </div>
                    </div>
                    <div class="card-footer">
                        <button type="button" class="btn btn-outline-primary btn-block" @click="submit">
                            <feather type="log-in" :size="14"></feather>
                            {{ isLogin ? "Login" : "Register" }}
                        </button>
                        <div class="text-center mt-1">
                            <small v-if="isLogin">
                                Or
                                <a href="javascript:void(0);" @click="showRegisterForm">
                                    register
                                </a>
                                first!
                            </small>
                            <small v-if="!isLogin">
                                Or
                                <a href="javascript:void(0);" @click="showLoginForm">
                                    login
                                </a>
                                with email and password!
                            </small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import apiRequest from "@/scripts/api/api-request";

    export default {
        data() {
            return {
                email: "",
                password: "",
                isLogin: true
            }
        },
        computed: {
            isAuthorized() {
                return this.$store.state.isAuthorized;
            },
        },
        methods: {
            showRegisterForm() {
                this.isLogin = false;
            },
            showLoginForm() {
                this.isLogin = true;
            },
            submit() {
                if (this.isLogin) {
                    apiRequest.login(this.email, this.password)
                        .then((user) => {
                            this.onSuccessLogin(user);
                        })
                        .catch(() => {
                        });
                } else {
                    apiRequest.register(this.email, this.password)
                        .then((user) => {
                            this.onSuccessLogin(user);
                        })
                        .catch(() => {
                        });
                }
            },
            onSuccessLogin(user) {
                this.$store.commit("setAuthorized", true);
                this.$store.commit("setCurrentUser", user);
                this.$router.push({name: "index"});
            }
        },
        mounted() {
            if (this.isAuthorized) {
                this.$router.push({name: "index"});
            }
        }
    }
</script>

<style scoped>

</style>
