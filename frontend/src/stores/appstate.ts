import { defineStore } from "pinia";

export const useAppState = defineStore('editor-state', {
    state: () => ({
        splashScreen: true,
        changelog: false,
        codeEditor: false,
        theme: 'dark'
    }),

    actions: {
        setSplashScreen(payload: boolean) {
            this.splashScreen = payload;
        },
        toggleTheme() {
            this.theme = this.theme === 'dark' ? 'light' : 'dark';
            if (this.theme === 'light') {
                document.documentElement.classList.remove('dark')
            } else {
                document.documentElement.classList.add('dark')
            }

            localStorage.setItem('color-theme', this.theme);
        },
        setTheme(payload: any) {
            this.theme = payload;
        }
    },

    getters: {
        getSplashScreen(state) {
            return state.splashScreen;
        },
        getSavedThemeMode(state) {
            return state.theme;
        }
    }
});