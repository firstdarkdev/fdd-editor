<template>
  <div class="absolute top-0 left-0 w-full h-full flex items-center justify-center text-black dark:text-white">

    <div class="text-center">
      <AppLogo class="mb-5" />
      <UAlert
        title="WARNING: Make a backup of your config when working with this editor!"
        class="w-full text-left mb-5 uppercase"
        color="error"
        icon="i-lucide-info"
        variant="soft" />

      <div class="flex items-center flex-col mb-5 gap-4 w-full p-5">
        <h1 class="font-bold">No config file selected. Choose an action below to get started</h1>

        <div class="flex items-center justify-center gap-2">
          <UButton label="Select Config File" color="primary" variant="subtle" size="lg" icon="i-lucide-file" @click="open()" />
          <UButton label="Embed Editor" color="info" variant="subtle" size="lg" icon="i-lucide-code" @click="openEmbedEditor()" />
        </div>
      </div>

      <p class="text-yellow-500 dark:text-yellow-300 text-sm">
        Supports:
        <b>Simple RPC (3+/4+), Simple Discord Link (3+) and Simple RPC Stardew Edition</b> configs
      </p>
    </div>

  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import {useEditor} from "../../stores/editor.ts";
import {useFileDialog} from "@vueuse/core";
import {BACKEND_URL} from "../../composables/EditorFunctions.ts";
import {useToast} from "@nuxt/ui/composables";
import {isInvalidField} from "../../composables/FieldUtils.ts";

const router = useRouter()
const toast = useToast()

const openEmbedEditor = () => {
  router.push('/embed_editor').then(() => {
    useEditor().setEmbedEditor(true)
  })
}

const { open, onChange } = useFileDialog({
  accept: '.toml'
});

onChange((files) => {
  if (files?.item(0)) {
    loadFile(files[0]);
  }
})

const loadFile = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)

  fetch(`${BACKEND_URL}/v1/parseupload`, {
    method: 'POST',
    body: formData
  })
    .then(async (res) => {
      const dt = await res.json()

      if (!dt.error) {
        useEditor().setConfig(dt.data)
        useEditor().setOriginalConfig(dt.data.original)
        useEditor().setConfigLoaded(true)
        useEditor().setCurrentSection(Object.keys(dt.data.config).filter(k => !isInvalidField(k))[0])
        toast.add({
          title: "Success",
          description: "Config has been loaded",
          color: "success",
          duration: 2000
        })
      } else {
        toast.add({
          title: "Error",
          description: dt.message,
          color: "error",
          duration: 2000
        })
      }
    })
    .catch((err) => {
      console.error(err)
      toast.add({
        title: "Error",
        description: err,
        color: "error",
        duration: 2000
      })
    })
}
</script>
