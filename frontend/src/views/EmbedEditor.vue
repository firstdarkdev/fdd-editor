<template>
  <div class="content-container" style="height: 100vh !important;" v-if="useEditor().isConfigLoaded || useEditor().getEmbedEditor">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">

        <div class="flex items-center justify-between mb-5">
          <h2 class="font-bold text-2xl">Simple Discord Link - Embed Editor</h2>
          <button type="button" @click="open()" class="focus:outline-none text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:ring-green-300 font-medium rounded-lg text-sm px-2 py-1.5 dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800"> Load</button>
        </div>

        <div>

          <Collapse collapse-title="Author" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
            <div>
              <div class="w-full mt-2">
                <h2>Author Name</h2>
                <input type="text" placeholder="Author Name" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="authorName" />
              </div>

              <div class="grid grid-cols-2 gap-2 mt-2">
                <div class="w-full">
                  <h2>Author Icon</h2>
                  <input type="url" placeholder="Author Icon" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="authorIcon" />
                </div>

                <div>
                  <h2>Author URL</h2>
                  <input type="url" placeholder="Author URL" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="authorUrl" />
                </div>
              </div>

            </div>
          </Collapse>

          <Collapse collapse-title="Body" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
            <div class="w-full mt-2">
              <h2>Title</h2>
              <input type="text" placeholder="Title" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="embedTitle" />
            </div>

            <div class="w-full mt-2">
              <h2>Body</h2>
              <textarea id="message" rows="4" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" placeholder="Write your thoughts here..." v-model="embedBody"></textarea>
            </div>

            <div class="grid grid-cols-2 gap-2 mt-2">
              <div class="w-full">
                <h2>Title URL</h2>
                <input type="url" placeholder="Title URL" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="embedUrl" />
              </div>

              <div>
                <h2>Color</h2>
                <div>
                  <div class="flex">
                    <input type="url" placeholder="Title URL" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="embedColor" />
                    <input type="color" placeholder="Color" class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="embedColor" />
                  </div>
                </div>
              </div>
            </div>

          </Collapse>

          <Collapse collapse-title="Images" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
            <div class="w-full mt-2">
              <h2>Thumbnail</h2>
              <input type="url" placeholder="Thumbnail URL" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="embedThumbnail" />
            </div>

            <div class="w-full mt-2">
              <h2>Image</h2>
              <input type="url" placeholder="Image URL" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="embedImage" />
            </div>

          </Collapse>

          <Collapse collapse-title="Footer" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
            <div>
              <div class="w-full mt-2">
                <h2>Footer Text</h2>
                <input type="text" placeholder="Footer Text" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="footerText" />
              </div>

              <div class="grid grid-cols-2 gap-2 mt-2">
                <div class="w-full">
                  <h2>Footer Icon</h2>
                  <input type="url" placeholder="Footer Icon URL" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="footerIconUrl" />
                </div>

                <div>
                  <h2>Add Timestamp</h2>
                  <fwb-toggle v-model="discordTimestamp" />
                </div>
              </div>

            </div>
          </Collapse>

          <Collapse collapse-title="Fields" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
            <template #button>
              <FontAwesomeIcon :icon="faPlus" class="cursor-pointer" @click="addField()" />
            </template>

            <div v-for="(field, index) in embedFields" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
              <div class="flex items-center justify-between border-b py-2">
                <h1>Field {{ index + 1 }}</h1>
                <FontAwesomeIcon :icon="faTrash" class="color-danger-500 cursor-pointer" @click="deleteField(index)" />
              </div>

              <div class="flex items-center gap-2">
                <div class="w-full mt-2">
                  <h2>Title</h2>
                  <input type="text" placeholder="Title" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" v-model="field.name" />
                </div>

                <div>
                  <h2>Inline</h2>
                  <fwb-toggle v-model="field.inline" />
                </div>
              </div>

              <div class="mt-2">
                <h2>Value</h2>
                <textarea id="message" rows="4" class="w-full border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" placeholder="Write your thoughts here..." v-model="field.text"></textarea>
              </div>
            </div>
          </Collapse>

          <div class="p-4 rounded-lg relative">
            <div class="flex items-center justify-end gap-2">
              <button type="button" @click="reloadWindow()" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">Reset</button>
              <button type="button" @click="generateEmbed()" class="focus:outline-none text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">Download</button>
            </div>
          </div>

        </div>
      </div>

      <div class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative" :class="useAppState().getSavedThemeMode == 'light' ? 'discord-light-theme' : ''">
        <DiscordMessage author="Simple Discord Link" avatar="blue" :bot="true" compactMode="false">
          <DiscordEmbed
            slot="embeds"
            :borderColor="embedColor"
            :embedTitle="parsePlaceholders(embedTitle)"
            :url="parsePlaceholders(embedUrl)"
            :thumbnail="parsePlaceholders(embedThumbnail)"
            :image="parsePlaceholders(embedThumbnail)"
            :timestamp="getTimestampValue()"
            :footerIcon="parsePlaceholders(footerIconUrl)"
            :author-name="parsePlaceholders(authorName)"
            :author-icon="parsePlaceholders(authorIcon)"
            :author-url="parsePlaceholders(authorUrl)"
          >
            <div v-if="embedBody" v-html="md.render(parsePlaceholders(embedBody))" />

            <DiscordEmbedFields v-if="embedFields.length > 0">
              <DiscordEmbedField v-for="field in embedFields" :fieldTitle="parsePlaceholders(field.name)" :inline="field.inline">{{ parsePlaceholders(field.text) }}</DiscordEmbedField>
            </DiscordEmbedFields>

            <template #footer v-if="footerText">{{ parsePlaceholders(footerText) }}</template>
          </DiscordEmbed>
        </DiscordMessage>

        <h2 class="font-bold mt-5">Embed Placeholders</h2>
        <div class="no-more-tailwind" v-html="md.render(placeholders)" />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useEditor } from '@/stores/editor'
import { nextTick, onMounted, reactive, ref } from 'vue'
import { DiscordEmbed, DiscordEmbedField, DiscordEmbedFields, DiscordMessage } from '@discord-message-components/vue'
import Collapse from '@/components/editor/Collapse.vue'
import markdownit from 'markdown-it'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faPlus, faTrash } from '@fortawesome/free-solid-svg-icons'
import { initFlowbite } from 'flowbite'
import { useFileDialog } from '@vueuse/core'
import { BACKEND_URL, downloadFile, saveConfigFile } from '@/composables/EditorFunctions'
import { useToast } from '@/stores/toaststore'
import { FwbToggle, FwbInput } from 'flowbite-vue'
import { useAppState } from '@/stores/appstate'

const md = markdownit({
  html: true,
  linkify: true,
  typographer: true
})

const placeholders = "1) `%author%` -> The name of the Player/Server. For example: `HypherionSA`\n" +
  "2) `%avatar%` -> The player/server avatar link\n" +
  "3) `%message_contents%` -> The actual contents of the message\n" +
  "4) `%username%` -> The raw username of the player/server. For example, `hypherionsa` or `server`\n" +
  "5) `%player_avatar%` -> Get the Player avatar in server messages (Join/Leave, Death etc)\n" +
  "6) `%player_name%` -> Get the name of the player in server messages (Join/Leave, Death etc)";

// Embed Data
const authorName = ref<string | undefined>("%author%")
const authorIcon = ref<string | undefined>("%avatar%")
const authorUrl = ref<string | undefined>(undefined)

const embedTitle = ref<string | undefined>(undefined)
const embedUrl = ref<string | undefined>(undefined)
const embedColor = ref<string>("#000000")

const embedBody = ref<string | undefined>("%message_contents%")

const embedThumbnail = ref<string | undefined>(undefined)
const embedImage = ref<string | undefined>(undefined)

const footerText = ref<string | undefined>(undefined)
const footerIconUrl = ref<string | undefined>(undefined)
const discordTimestamp = ref(false)

interface embedField {
  name: string,
  text: string,
  inline: boolean
}

const getTimestampValue = () => {
  if (discordTimestamp.value == false) return undefined;

  return new Date().toDateString()
}

const embedFields = reactive<embedField[]>([])

const parsePlaceholders = (data: string | undefined) => {
  if (!data)
    return undefined;

  data = data.replace("%author%", "HypherionSA");
  data = data.replace("%avatar%", "https://mc-heads.net/head/c973a5eb-2012-46ff-9eed-1767b5ccba71/512")
  data = data.replace("%message_contents%", "Hello Minecraft people! How are you today?")
  data = data.replace("%player_avatar%", "https://mc-heads.net/head/c973a5eb-2012-46ff-9eed-1767b5ccba71/512")
  data = data.replace("%player_name%", "TheRealHypherionSA")
  data = data.replace("%current_time%", "TBD")
  data = data.replace("%username%", "server")

  return data;
}

const addField = async () => {
  embedFields.push({
    name: "Field",
    text: "Body",
    inline: true
  })

  await nextTick();
  initFlowbite();
}

const deleteField = (index: number) => {
  embedFields.splice(index, 1);
}

const cleanEmbedValues = (value: string | undefined | null) => {
  if (value == null)
    return undefined;

  if (!value)
    return null;

  return value;
}

const generateEmbedFields = () => {
  const fields: { name: string; value: string; inline: boolean }[] = [];

  for (const field of embedFields) {
    if (field.name.trim() && field.text.trim()) {
      fields.push({
        name: field.name,
        value: field.text,
        inline: field.inline
      });
    }
  }

  return fields.length ? fields : null;
};

const generateEmbed = () => {
  const embed = {
    title: cleanEmbedValues(embedTitle.value),
    description: cleanEmbedValues(embedBody.value),
    color: embedColor.value,
    timestamp: 0,
    url: cleanEmbedValues(embedUrl.value),
    author: authorName.value == undefined ? null : {
      name: authorName.value,
      url: cleanEmbedValues(authorUrl.value),
      icon_url: cleanEmbedValues(authorIcon.value)
    },
    thumbnail: embedThumbnail.value == undefined ? null : {
      url: embedThumbnail.value
    },
    image: embedImage.value == undefined ? null : {
      url: embedImage.value
    },
    footer: (footerText.value == undefined || footerIconUrl.value == undefined) ? null : {
      text: cleanEmbedValues(footerText.value),
      icon_url: cleanEmbedValues(footerIconUrl.value)
    },
    fields: embedFields.length == 0 ? null : generateEmbedFields()
  };

  useEditor().embedJson = JSON.stringify(embed, null, 2);
  saveConfigFile(false, true)
}

onMounted(() => {
  initFlowbite()
  useEditor().setEmbedEditor(true)
})

const { open, onChange } = useFileDialog({
  accept: '.json'
});

onChange((files) => {
  if (files?.item(0)) {
    const formData = new FormData();
    formData.append('file', files[0]);

    fetch(`${BACKEND_URL}/v1/parseembed`, {
      method: 'POST',
      body: formData
    }).then(async res => {
      const dt = await res.json();

      if (!dt.error) {
        const data = JSON.parse(dt.data)

        authorName.value = data.author == null ? undefined : (data.author.name || undefined)
        authorUrl.value = data.author == null ? undefined : (data.author.url || undefined)
        authorIcon.value = data.author == null ? undefined : (data.author.icon_url || undefined)

        embedTitle.value = data.title == null ? undefined : data.title
        embedUrl.value = data.url == null ? undefined : data.url
        embedBody.value = data.description == null ? undefined : data.description
        embedColor.value = data.color || "#000000"

        embedThumbnail.value = data.thumbnail == null ? undefined : (data.thumbnail.url || undefined)
        embedImage.value = data.image == null ? undefined : (data.image.url || undefined)

        footerText.value = data.footer == null ? undefined : (data.footer.text || undefined)
        footerIconUrl.value = data.footer == null ? undefined : (data.footer.icon_url || undefined)

        if (data.fields) {
          for (const ff of data.fields) {
            const field = ff as any;

            embedFields.push({
              name: field.name,
              text: field.value,
              inline: field.inline || false
            })
          }
        }

        // TODO Timestamp

        useToast().showToast('Success', 2000, "success");
      } else {
        useToast().showToast(dt.message, 2000, "error");
      }
    }).catch(err => {
      useToast().showToast(err, 2000, "error");
    })
  }
})

const reloadWindow = () => {
  window.location.reload()
}
</script>

<style lang="css">
code {
  background: rgb(53 53 53) !important;
  padding: 0.1rem 0.5rem;
  color: white !important;
  border-radius: 5px;
}
</style>