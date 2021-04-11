<template>
    <div></div>
</template>

<script>
    import apiRequest from "@/scripts/api/api-request";

    export default {
        mounted() {
            apiRequest.logout()
                .catch(() => {
                    this.$store.commit("setAuthorized", false);
                    this.$router.push({name: "login"});
                });
        }
    }
</script>

<style scoped>

</style>
