<template>
    <div>
        <c-preloader-with-sections-list>
            <template v-if="sectionsList">
                <div class="sections-list" :key="rerenderKey">
                    <template v-if="isEditMode && hasNotFinalSections">
                        <label>
                            <input type="checkbox" v-model="showOnlyFinalSections"
                                   @change="onShowOnlyFinalSectionsChange">
                            Show only final sections
                        </label>
                    </template>
                    <template v-for="(section, sectionIndex) in sectionsList">
                        <template v-if="!isEditMode && section.entries.length > 0">
                            <c-section :section="section" :key="sectionIndex">
                                <c-view :section="section"/>
                            </c-section>
                        </template>
                        <template v-if="isEditMode && isShowSectionInEditMode(section)">
                            <c-section :section="section" :key="sectionIndex">
                                <c-edit :section="section"/>
                            </c-section>
                        </template>
                    </template>
                    <c-actions/>
                </div>
            </template>
        </c-preloader-with-sections-list>
    </div>
</template>

<script>
    import cSection from "@/scripts/vue/pages/list/section";
    import cView from "@/scripts/vue/pages/list/view";
    import cEdit from "@/scripts/vue/pages/list/edit";
    import cActions from "@/scripts/vue/pages/list/actions";
    import cPreloaderWithSectionsList from "@/scripts/vue/pages/common/preloader-with-sections-list";

    export default {
        components: {
            cPreloaderWithSectionsList,
            cSection,
            cView,
            cEdit,
            cActions,
        },
        data() {
            return {
                rerenderKey: Math.random(),
                showOnlyFinalSections: true,
            }
        },
        computed: {
            isEditMode() {
                return this.$store.state.isEditMode;
            },
            sectionsList() {
                return this.$store.state.sectionsList;
            },
            hasNotFinalSections() {
                for (let section of this.sectionsList) {
                    if (!section.isFinal) {
                        return true;
                    }
                }
                return false;
            }
        },
        methods: {
            isShowSectionInEditMode(section) {
                if (!this.showOnlyFinalSections) {
                    return true;
                }
                return section.isFinal;
            },
            rerender() {
                this.rerenderKey = Math.random();
            },
            onShowOnlyFinalSectionsChange() {
                this.rerender();
            }
        }
    }
</script>

<style scoped>

</style>
