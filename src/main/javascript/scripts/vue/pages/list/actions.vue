<template>
    <div class="card mt-4" v-if="isEditMode && hasChanges">
        <div class="card-header">
            Available action
        </div>
        <div class="card-body">
            You have changed the data. Please, save or discard changes.
        </div>
        <div class="card-footer text-right">
            <button class="btn btn-outline-secondary btn-sm"
                    @click="discard">
                <feather type="x-square" :size="18"></feather>
                Discard
            </button>
            <button class="btn btn-outline-primary btn-sm"
                    :disabled="performing"
                    @click="save">
                <template v-if="!performing">
                    <feather type="save" :size="18"></feather>
                    Save
                </template>
                <template v-if="performing">
                    <feather type="refresh-cw" :size="18" animation="spin"></feather>
                    Saving...
                </template>
            </button>
        </div>
    </div>
</template>

<script>
    import {equals} from "@/scripts/functions/common";
    import ApiRequest from "@/scripts/api/api-request";

    export default {
        components: {},
        props: {},
        data() {
            return {
                performing: false,
            }
        },
        computed: {
            sectionsList() {
                return this.$store.state.sectionsList;
            },
            selectedSectionsIds() {
                return this.$store.state.selectedSections;
            },
            sectionsListOriginal() {
                return this.$store.state.sectionsListOriginal;
            },
            isEditMode() {
                return this.$store.state.isEditMode;
            },
            hasChanges() {
                return !equals(this.sectionsList, this.sectionsListOriginal);
            }
        },
        methods: {
            loadSectionsList() {
                return new Promise((resolve, reject) => {
                    ApiRequest.getSectionsList(this.selectedSectionsIds)
                        .then((sectionsList) => {
                            resolve(sectionsList);
                        })
                        .catch(() => {
                            reject();
                        });
                });
            },
            discard() {
                this.$store.commit("setSectionsList", this.sectionsListOriginal);
                this.$store.commit("setEditMode", false);
            },
            save() {
                this.performing = true;
                ApiRequest.updateSectionsList(this.sectionsList)
                    .then(() => this.loadSectionsList())
                    .then((sectionsList) => {
                        this.$store.commit("setSectionsList", sectionsList);
                        this.$store.commit("setSectionsListOriginal", sectionsList);
                        this.$store.commit("setEditMode", false);
                        this.performing = false;
                    })
                    .catch(() => {
                        this.performing = false;
                    });
            }
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>
