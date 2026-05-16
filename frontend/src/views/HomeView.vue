<template>
  <div class="flex flex-1">
    <USidebar variant="sidebar" v-if="useEditor().isConfigLoaded">
      <template #title>
       <div class="flex items-center gap-2">
         <AppLogo />
         <UBadge variant="subtle" size="sm" color="success" label="Beta" />
       </div>
      </template>

      <div class="flex flex-col cursor-pointer overflow-x-auto" v-if="useEditor().isConfigLoaded">
        <UButton
          variant="link"
          :color="useEditor().getCurrentSection === key ? 'info' : 'neutral'"
          v-for="key in Object.keys(useEditor().getConfig.config as any).filter(k => !isInvalidField(k))"
          :label="headerToDisplay(key)"
          @click="useEditor().setCurrentSection(key)"
        />
      </div>
    </USidebar>

    <div class="flex-1 flex flex-col w-full">
      <EditorHeader :title="sectionTitle" v-if="useEditor().isConfigLoaded || useEditor().getEmbedEditor" class="w-full" />

      <div class="content-container" v-if="useEditor().isConfigLoaded">
        <div class="rounded-lg relative">
          <div class="editor-body mt-5 flex gap-2 flex-col">
            <ConfigNodeEditor
              :value="useEditor().getConfig.config[useEditor().currentSection]"
              :comments="useEditor().getConfig.comments"
              :path="[useEditor().currentSection]"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {useAppState} from "../stores/appstate.ts";
import {useEditor} from "../stores/editor.ts";
import {BACKEND_URL} from "../composables/EditorFunctions.ts";
import {headerToDisplay, isInvalidField} from "../composables/FieldUtils.ts";
import {useToast} from "@nuxt/ui/composables";

const props = defineProps(['id'])
const toast = useToast()

if (props.id) {
  const ws = new WebSocket(`${BACKEND_URL}/ws/frontend?identifier=${props.id}`);
  useAppState().setIdentifier(props.id);

  ws.onopen = () => {
    ws.send(JSON.stringify({
      socketCode: "WS_CHECK_SESSION",
      identifier: props.id
    }));

    setInterval(() => ws.send("ping"), 20000);
  }

  ws.onmessage = (event) => {
    const data = JSON.parse(event.data);

    if (data.socketCode === 'WS_VALID_SESSION') {
      toast.add({
        title: "Success",
        description: "Session opened. Waiting for data",
        color: 'success',
        duration: 2000
      })

      ws.send(JSON.stringify({
        socketCode: "WS_GET_CONFIG",
        identifier: props.id
      }));
    }

    if (data.socketCode === 'WS_INVALID_SESSION') {
      toast.add({
        title: 'Error',
        description: "Invalid Session. Please try again",
        duration: 2000,
        color: "error"
      })
    }

    if (data.socketCode === 'WS_CONFIG_ERROR') {
      toast.add({
        title: "Websocket Error",
        description: data.message,
        color: "error",
        duration: 2000
      })
    }

    if (data.socketCode === 'WS_SEND_CONFIG') {
      const dt = JSON.parse(data.message);
      useAppState().setSplashScreen(false);
      useEditor().setConfig(dt);
      useEditor().setSocketConfig(true);
      useEditor().setConfigLoaded(true);
      useEditor().setCurrentSection(Object.keys(dt.config)[0]);
      toast.add({
        title: 'Success',
        color: 'success',
        duration: 2000
      })
    }
  }

  ws.onclose = () => {
    useEditor().setConfigLoaded(false);
    toast.add({
      title: "Session Closed",
      description: "Web Socket Connection Terminated",
      color: "error",
      duration: 2000
    })

    setTimeout(() => {
      window.location.href = "/";
    }, 2000);
  }
}

const sectionTitle = computed(() => {
  return headerToDisplay(useEditor().currentSection)
})
</script>
