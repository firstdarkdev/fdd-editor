import {defineStore} from "pinia";

export const useEditor = defineStore('editor-editor', {
    state: () => ({
        hasConfigLoaded: false,
        currentSection: undefined,
        configData: undefined,
        savedConfig: false,
        tomlConfig: ""
    }),

    actions: {
        setConfigLoaded(payload: boolean) {
            this.hasConfigLoaded = payload;
        },
        setCurrentSection(payload: any) {
            this.currentSection = payload;
        },
        setConfig(payload: any) {
            this.configData = payload;
        },
        setTomlConfig(payload: any) {
            if (payload == undefined) {
                this.savedConfig = false;
                this.tomlConfig = "";
            } else {
                this.savedConfig = true;
                this.tomlConfig = payload;
            }
        }
    },

    getters: {
        isConfigLoaded(state) {
           return state.hasConfigLoaded;
        },
        getCurrentSection(state) {
           return state.currentSection;
        },
        getConfig(state) {
           return state.configData;
        },
        getTomlConfig(state) {
            return state.tomlConfig;
        },
        hasSavedConfig(state) {
            return state.savedConfig;
        }
    }
});