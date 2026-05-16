<template>
  <img :src="themeColor == 'dark' ? LOGODARK : LOGO" class="block w-full" alt="logo" />
</template>

<script setup lang="ts">
import {computed} from "vue";
import {useColorMode} from "@vueuse/core";
import LOGODARK from '../assets/img/config_editor.svg'
import LOGO from '../assets/img/config_editor_light.svg'

const colorMode = useColorMode()
const themeColor = computed(() => colorMode.value)
</script>
