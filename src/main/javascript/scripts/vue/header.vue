<template>
    <div v-if="isAuthorized">
        <input type="file" ref="inputImport" class="d-none" accept="application/JSON"
               v-on:change="onImport"/>
        <nav class="site-header sticky-top py-1">
            <div class="container clearfix">
                <div class="float-left">
                    <template v-if="this.$route.name !== 'tree'">
                        <router-link to="/tree" class="btn btn-outline-secondary d-inline-block d-md-none not-focusable">
                            <feather type="home" :size="14"></feather>
                        </router-link>
                    </template>
                    <router-link to="/tree" class="btn btn-link d-none d-md-inline-block ml-2 not-focusable">
                        Sections
                    </router-link>
                    <template v-if="this.$route.name === 'list'">
                        <router-link to="/list" class="btn btn-link d-none d-md-inline-block ml-2 not-focusable">
                            <template v-if="!isEditMode">Viewer</template>
                            <template v-if="isEditMode">Editor</template>
                        </router-link>
                    </template>
                    <template v-if="this.$route.name === 'cards'">
                        <router-link to="/cards" class="btn btn-link d-inline-block ml-2 not-focusable">
                            Cards
                        </router-link>
                    </template>
                    <template v-if="this.$route.name !== 'cards' && (sectionsTree || sectionsList)">
                        <button class="btn btn-outline-secondary ml-md-2 not-focusable"
                                :class="{ 'active': isEditMode, 'disabled': hasChanges || performing }"
                                @click="toggleEditMode"
                                v-tooltip="hasChanges ? editModeHelpText : ''">
                            <feather type="toggle-left" :size="14" v-if="!isEditMode"></feather>
                            <feather type="toggle-right" :size="14" v-if="isEditMode"></feather>
                            Edit
                        </button>
                    </template>
                </div>
                <div class="float-right not-focusable">
                    <router-link to="/test" class="btn btn-link d-none d-md-inline-block ml-2 not-focusable" v-if="isDev">
                        Test
                    </router-link>
                    <button class="btn btn-outline-secondary ml-2 not-focusable"
                            :class="{ 'active' : isDarkMode }"
                            v-tooltip="!isDarkMode ? 'Turn on the dark mode' : 'Turn off the dark mode'"
                            @click="toggleDarkMode">
                        <feather type="moon" :size="14"></feather>
                    </button>
                    <template v-if="this.$route.name === 'tree'">
                        <button class="btn btn-outline-secondary ml-2 d-none d-md-inline-block not-focusable"
                                @click="importOrExport"
                                :class="{ 'disabled': performing }">
                            <template v-if="!performing">
                                <feather type="upload" :size="14" v-if="!isEditMode"></feather>
                                <feather type="download" :size="14" v-if="isEditMode"></feather>
                                <span class="d-none d-md-inline-block">
                                    {{ !isEditMode ? "Export" : "Import" }}
                                </span>
                            </template>
                            <template v-if="performing">
                                <feather type="refresh-cw" :size="14" animation="spin"></feather>
                                <span class="d-none d-md-inline-block">
                                    {{ !isEditMode ? "Exporting..." : "Importing..." }}
                                </span>
                            </template>
                        </button>
                    </template>
                    <router-link to="/logout" class="btn btn-outline-secondary d-inline-block ml-2">
                        <feather type="log-out" :size="14"></feather>
                        <span class="d-none d-md-inline-block">
                            Logout
                        </span>
                    </router-link>
                </div>
            </div>
        </nav>
    </div>
</template>

<script>
    import {download, equals} from "@/scripts/functions/common";
    import {isDev} from "@/scripts/functions/dev";
    import {isSectionsTreesEqual} from "@/scripts/functions/section";
    import ApiRequest from "@/scripts/api/api-request";
    import iconLogo from "@/images/logo.ico";
    import pngLogo from "@/images/logo.png";

    export default {
        data() {
            return {
                performing: false,
                iconLogo,
                pngLogo
            }
        },
        computed: {
            isAuthorized() {
                return this.$store.state.isAuthorized;
            },
            isEditMode() {
                return this.$store.state.isEditMode;
            },
            isDarkMode() {
                return this.$store.state.isDarkMode;
            },
            isDev() {
                return isDev();
            },
            sectionsTree() {
                return this.$store.state.sectionsTree;
            },
            sectionsTreeOriginal() {
                return this.$store.state.sectionsTreeOriginal;
            },
            sectionsList() {
                return this.$store.state.sectionsList;
            },
            sectionsListOriginal() {
                return this.$store.state.sectionsListOriginal;
            },
            hasChanges() {
                let hasTreeChanges = false;
                if (this.sectionsTree !== null) {
                    hasTreeChanges = !isSectionsTreesEqual(this.sectionsTree, this.sectionsTreeOriginal);
                }
                let hasListChanges = !equals(this.sectionsList, this.sectionsListOriginal);
                return hasTreeChanges || hasListChanges;
            },
            editModeHelpText() {
                if (this.performing) {
                    return "Wait until the request is finished!";
                }
                return "You need either save or discard changes before disabling edit mode!";
            }
        },
        methods: {
            toggleEditMode() {
                if (this.hasChanges) {
                    return;
                }
                this.$store.commit("setEditMode", !this.isEditMode);
            },
            importOrExport() {
                if (!this.isEditMode) {
                    this.export();
                } else {
                    this.import();
                }
            },
            toggleDarkMode() {
                this.$store.commit("setDarkMode", !this.isDarkMode);
            },
            export() {
                this.performing = true;
                ApiRequest.exportAll()
                    .then((fileName) => {
                        download(fileName);
                        this.performing = false;
                    })
                    .catch(() => {
                        this.performing = false;
                    });
            },
            import() {
                this.$refs.inputImport.click();
            },
            onImport() {
                let formData = new FormData();
                formData.append("file", this.$refs.inputImport.files[0]);
                this.performing = true;
                ApiRequest.importAll(formData)
                    .then(() => ApiRequest.getSectionsTree())
                    .then((sectionsTree) => {
                        this.$store.commit("setSectionsTree", sectionsTree);
                        this.$store.commit("setSectionsTreeOriginal", sectionsTree);
                        this.$store.commit("setEditMode", false);
                        this.performing = false;
                    })
                    .catch(() => {
                        this.performing = false;
                    });
            }
        }
    }
</script>

<style scoped>

</style>
