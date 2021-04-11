import Vuex from "vuex";
import Vue from "vue";

import logger from "@/scripts/service/logger";
import {addRequiredFields, removeUntrackableFields} from "@/scripts/functions/section";
import {clone} from "@/scripts/functions/common";

Vue.use(Vuex);

const store = new Vuex.Store({
    state: {
        currentUser: null,
        isSessionChecked: false,
        isAuthorized: false,
        isEditMode: false,
        isDarkMode: false,
        sectionsTree: null,
        sectionsTreeOriginal: null,
        selectedSections: [],
        sectionsList: null,
        sectionsListOriginal: null,
    },
    mutations: {
        setAuthorized(state, isAuthorized) {
            logger.log("Set state for 'isAuthorized':", isAuthorized);
            state.isAuthorized = isAuthorized;
            state.isSessionChecked = true;
        },
        setCurrentUser(state, user) {
            logger.log("Set state for 'user':", user);
            state.currentUser = user;
        },
        setEditMode(state, isEditMode) {
            logger.log("Set state for 'isEditMode':", isEditMode);
            state.isEditMode = isEditMode;
        },
        setDarkMode(state, isDarkMode) {
            logger.log("Set state for 'isDarkMode':", isDarkMode);
            state.isDarkMode = isDarkMode;
        },
        setSectionsTree(state, sectionsTree) {
            sectionsTree = clone(sectionsTree);
            sectionsTree = addRequiredFields(sectionsTree);
            logger.log("Set state for 'sectionsTree':", sectionsTree);
            state.sectionsTree = sectionsTree;
        },
        setSectionsTreeOriginal(state, sectionsTreeOriginal) {
            sectionsTreeOriginal = clone(sectionsTreeOriginal);
            sectionsTreeOriginal = removeUntrackableFields(sectionsTreeOriginal);
            logger.log("Set state for 'sectionsTreeOriginal':", sectionsTreeOriginal);
            state.sectionsTreeOriginal = sectionsTreeOriginal;
        },
        setSectionsList(state, sectionsList) {
            sectionsList = clone(sectionsList);
            logger.log("Set state for 'sectionsList':", sectionsList);
            state.sectionsList = sectionsList;
        },
        setSectionsListOriginal(state, sectionsListOriginal) {
            sectionsListOriginal = clone(sectionsListOriginal);
            logger.log("Set state for 'sectionsListOriginal':", sectionsListOriginal);
            state.sectionsListOriginal = sectionsListOriginal;
        },
        setSelectedSections(state, selectedSections) {
            selectedSections = clone(selectedSections);
            logger.log("Set state for 'selectedSections':", selectedSections);
            state.selectedSections = selectedSections;
        }
    }
});

export default store;
