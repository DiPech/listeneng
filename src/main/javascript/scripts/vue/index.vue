<template>
    <div class="pb-4" :class="{ 'dark-mode' : isDarkMode }" id="index">
        <c-preloader :condition="isSessionChecked">
            <c-header></c-header>
            <div class="container mt-4">
                <router-view></router-view>
            </div>
        </c-preloader>
    </div>
</template>

<script>
    import cHeader from "@/scripts/vue/header";
    import cPreloader from "@/scripts/vue/partials/preloader";
    import {checkSessionAndFetchCurrentUser} from "@/scripts/functions/session";

    export default {
        components: {
            cHeader,
            cPreloader,
        },
        computed: {
            isSessionChecked() {
                return this.$store.state.isSessionChecked;
            },
            isDarkMode() {
                return this.$store.state.isDarkMode;
            }
        },
        mounted() {
            checkSessionAndFetchCurrentUser();
        }
    }
</script>

<style scoped>

</style>
