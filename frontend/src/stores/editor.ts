import { defineStore } from "pinia";

export const useEditor = defineStore('editor-editor', {
    state: () => ({
        hasConfigLoaded: false,
        currentSection: undefined,
        configData: undefined,
        savedConfig: false,
        tomlConfig: "",
        isSocketConfig: false,
        downloadConfig: {
            original: '',
            modified: '',
            isSaved: false,
        }
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
        setSocketConfig(payload: any) {
            this.isSocketConfig = payload;
        },
        setTomlConfig(payload: any) {
            if (payload == undefined) {
                this.savedConfig = false;
                this.tomlConfig = "";
            } else {
                this.savedConfig = true;
                this.tomlConfig = payload;
            }
        },
        setOriginalConfig(payload: string) {
          this.downloadConfig.original = payload;
        },
        setDownloadConfig(payload: boolean, modified: string) {
            this.downloadConfig.isSaved = payload;
            this.downloadConfig.modified = modified;
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
        },
        checkIsSocketConfig(state) {
            return state.isSocketConfig;
        },
        hasDownloadConfig(state) {
            return state.downloadConfig;
        }
    }
});