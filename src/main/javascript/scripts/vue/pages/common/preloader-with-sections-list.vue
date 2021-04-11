<template>
    <div>
        <c-preloader :condition="sectionsList">
            <template v-if="sectionsList">
                <slot/>
            </template>
        </c-preloader>
    </div>
</template>

<script>
    import cPreloader from "@/scripts/vue/partials/preloader";
    import ApiRequest from "@/scripts/api/api-request";
    import {preventGuests} from "@/scripts/functions/session";
    import {getPredefinedSectionId, isDev, isProd} from "@/scripts/functions/dev";

    export default {
        components: {
            cPreloader,
        },
        data() {
            return {}
        },
        computed: {
            selectedSectionsIds() {
                return this.$store.state.selectedSections;
            },
            sectionsList() {
                return this.$store.state.sectionsList;
            }
        },
        methods: {
            getSelectedSectionsIds() {
                if (isProd()) {
                    return this.selectedSectionsIds;
                }
                if (isDev()) {
                    if (this.selectedSectionsIds.length > 0) {
                        return this.selectedSectionsIds;
                    }
                    let selectedSectionsIds = [getPredefinedSectionId()];
                    this.$store.commit("setSelectedSections", selectedSectionsIds);
                    return selectedSectionsIds;
                }
            }
        },
        mounted() {
            preventGuests()
                .then(() => ApiRequest.getSectionsList(this.getSelectedSectionsIds()))
                .then((sectionsList) => {
                    if (sectionsList.length === 0) {
                        this.$toasted.error("Couldn't load any sections :(");
                        this.$router.push({name: "tree"});
                        return;
                    }
                    this.$store.commit("setSectionsList", sectionsList);
                    this.$store.commit("setSectionsListOriginal", sectionsList);
                });
        },
        destroyed() {
            this.$store.commit("setSelectedSections", []);
            this.$store.commit("setSectionsList", null);
            this.$store.commit("setSectionsListOriginal", null);
        }
    }
</script>

<style scoped>

</style>
