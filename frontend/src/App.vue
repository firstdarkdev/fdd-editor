<script setup lang="ts">
import { RouterView } from 'vue-router'
import LoadingSplash from "@/components/splashes/LoadingSplash.vue";
import {useAppState} from "@/stores/appstate";
import NoConfigSplash from "@/components/splashes/NoConfigSplash.vue";
import Toast from "@/components/toast/Toast.vue";
import {useEditor} from "@/stores/editor";
import EditorHeader from "@/components/editor/EditorHeader.vue";
import { computed, onMounted } from 'vue'
import {initFlowbite} from "flowbite";
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faMoon, faSun } from '@fortawesome/free-solid-svg-icons'
import SavedConfigPopup from '@/components/editor/SavedConfigPopup.vue'

onMounted(() => {
  initFlowbite();

  if (localStorage.getItem('color-theme') != undefined) {
    useAppState().setTheme(localStorage.getItem('color-theme'))
  }
})

const themeMode = computed(() => {
  return useAppState().getSavedThemeMode === 'dark';
})
</script>

<template>
  <Toast />
  <SavedConfigPopup />
  <EditorHeader v-if="useEditor().isConfigLoaded" />

  <LoadingSplash v-if="useAppState().getSplashScreen" />
  <NoConfigSplash v-if="!useEditor().isConfigLoaded" />
  <RouterView />

  <div
    class="fixed z-10 right-4 bottom-4 flex items-center justify-center cursor-pointer"
    style="font-size: 20px; width: 40px; height: 40px; border-radius: 50%;"
    :class="themeMode ? 'bg-yellow-300' : 'bg-blue-950'"
    @click="useAppState().toggleTheme()"
  >
    <FontAwesomeIcon :icon="faSun" v-if="themeMode" class="text-black" />
    <FontAwesomeIcon :icon="faMoon" v-if="!themeMode" class="text-yellow-300" />
  </div>
</template>