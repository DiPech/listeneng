<template>
    <div>
        <c-preloader :condition="sectionsTree">
            <template v-if="sectionsTree">
                <c-node :root="sectionsTree" :list="sectionsTree"/>
                <c-actions/>
            </template>
        </c-preloader>
    </div>
</template>

<script>
    import cPreloader from "@/scripts/vue/partials/preloader";
    import cNode from "@/scripts/vue/pages/tree/node";
    import cActions from "@/scripts/vue/pages/tree/actions";
    import ApiRequest from "@/scripts/api/api-request";
    import {preventGuests} from "@/scripts/functions/session";

    export default {
        components: {
            cPreloader,
            cNode,
            cActions
        },
        data() {
            return {}
        },
        computed: {
            sectionsTree: {
                get() {
                    return this.$store.state.sectionsTree;
                },
                set(value) {
                    this.$store.commit("setSectionsTree", value);
                }
            }
        },
        methods: {},
        mounted() {
            preventGuests()
                .then(() => ApiRequest.getSectionsTree())
                .then((sectionsTree) => {
                    this.$store.commit("setSectionsTree", sectionsTree);
                    this.$store.commit("setSectionsTreeOriginal", sectionsTree);
                });
        }
    }
</script>

<style scoped>

</style>
