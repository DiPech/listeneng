<template>
    <div class="card mt-4" v-if="hasChanges || atLeastOneSelected">
        <div class="card-header">
            Available actions
            <template v-if="checkedCount > 0">
                ({{ checkedCount }} selected)
            </template>
        </div>
        <div class="card-body">
            <div v-for="action in actionsList" class="form-group mb-0">
                <label>
                    <input type="radio" name="action"
                           :value="action.name" :key="action.name"
                           @change="onActionChange($event.target.value)">
                    {{ action.title }}
                </label>
            </div>
        </div>
        <div class="card-footer text-right">
            <button class="btn btn-outline-secondary btn-sm"
                    @click="discard">
                <feather type="x-square" :size="18"></feather>
                Discard
            </button>
            <button class="btn btn-outline-primary btn-sm"
                    v-if="action || performing"
                    :disabled="performing"
                    @click="perform">
                <template v-if="!performing">
                    <feather type="play-circle" :size="18"></feather>
                    Perform
                </template>
                <template v-if="performing">
                    <feather type="refresh-cw" :size="18" animation="spin"></feather>
                    Performing...
                </template>
            </button>
        </div>
    </div>
</template>

<script>
    import {
        calcSectionsCheckedChildren,
        deselectAllSections,
        getSelectedSectionsIds,
        isSectionsTreesEqual
    } from "@/scripts/functions/section";
    import ApiRequest from "@/scripts/api/api-request"
    import AudioPlayer from "@/scripts/service/player";

    export default {
        components: {},
        props: {},
        data() {
            return {
                performing: false,
                action: null
            }
        },
        computed: {
            sectionsTree() {
                return this.$store.state.sectionsTree;
            },
            sectionsTreeOriginal() {
                return this.$store.state.sectionsTreeOriginal;
            },
            hasChanges() {
                return !isSectionsTreesEqual(this.sectionsTree, this.sectionsTreeOriginal);
            },
            atLeastOneSelected() {
                return this.checkedCount > 0;
            },
            bulkActionsAvailable() {
                return this.checkedCount > 1;
            },
            checkedCount() {
                return calcSectionsCheckedChildren(this.sectionsTree);
            },
            isEditMode() {
                return this.$store.state.isEditMode;
            },
            actionsList() {
                let result = [];
                if (this.isEditMode) {
                    if (this.hasChanges) {
                        result.push({
                            name: "save",
                            title: "Save changes",
                        });
                    }
                    if (this.atLeastOneSelected) {
                        result.push({
                            name: "bulk-edit",
                            title: "Edit all",
                        });
                        result.push({
                            name: "bulk-delete",
                            title: "Delete all",
                        });
                    }
                } else if (this.atLeastOneSelected) {
                    result.push({
                        name: "bulk-view",
                        title: "View all",
                    });
                    result.push({
                        name: "bulk-listen",
                        title: "Listen to all",
                    });
                    result.push({
                        name: "bulk-learn",
                        title: "Learn all",
                    });
                }
                return result;
            }
        },
        methods: {
            onActionChange(action) {
                this.action = action;
            },
            discard() {
                this.action = null;
                this.$store.commit("setSectionsTree", this.sectionsTreeOriginal);
                this.$store.commit("setEditMode", false);
            },
            loadSectionsTree() {
                return new Promise((resolve, reject) => {
                    ApiRequest.getSectionsTree()
                        .then((sectionsTree) => {
                            this.$store.commit("setSectionsTree", sectionsTree);
                            this.$store.commit("setSectionsTreeOriginal", sectionsTree);
                            resolve();
                        })
                        .catch(() => {
                            reject();
                        });
                });
            },
            perform() {
                if (this.action === "bulk-view") {
                    this.view(() => {
                        this.onPerformFinish(false);
                    });
                    return;
                }
                if (this.action === "bulk-edit") {
                    this.view(() => {
                        this.onPerformFinish(true);
                    });
                    return;
                }
                if (this.action === "bulk-learn") {
                    this.learn(() => {
                        this.onPerformFinish(false);
                    });
                    return;
                }
                if (this.action === "bulk-listen") {
                    this.listen(() => {
                        this.onPerformFinish(false);
                    });
                    return;
                }
                this.performing = true;
                Promise.resolve()
                    .then(() => {
                        if (this.action === "save") {
                            return this.save();
                        } else if (this.action === "bulk-delete") {
                            return this.delete();
                        }
                    })
                    .then(() => {
                        this.onPerformFinish(false);
                    })
                    .catch(() => {
                        this.performing = false;
                    });
            },
            onPerformFinish(editMode) {
                this.$store.commit("setEditMode", editMode);
                this.performing = false;
                this.action = null;
                deselectAllSections(this.sectionsTree);
            },
            save() {
                return new Promise(((resolve, reject) => {
                    ApiRequest.updateSectionsTree(this.sectionsTree)
                        .then(() => this.loadSectionsTree())
                        .then(() => {
                            resolve();
                        })
                        .catch(() => {
                            reject();
                        });
                }));
            },
            delete() {
                return new Promise(((resolve, reject) => {
                    ApiRequest.deleteSections(getSelectedSectionsIds(this.sectionsTree))
                        .then(() => this.loadSectionsTree())
                        .then(() => {
                            resolve();
                        })
                        .catch(() => {
                            reject();
                        });
                }));
            },
            listen(callback) {
                this.$store.commit("setSelectedSections", getSelectedSectionsIds(this.sectionsTree));
                callback();
                this.$router.push({name: "listen"});
            },
            view(callback) {
                this.$store.commit("setSelectedSections", getSelectedSectionsIds(this.sectionsTree));
                callback();
                this.$router.push({name: "list"});
            },
            learn(callback) {
                this.$store.commit("setSelectedSections", getSelectedSectionsIds(this.sectionsTree));
                callback();
                this.$router.push({name: "cards"});
            }
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>
