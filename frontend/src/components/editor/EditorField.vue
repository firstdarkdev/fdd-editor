<template>
  <div class="flex gap-4 mt-2 items-center">
    <div class="bg-ct-card-light dark:bg-ct-card-dark p-4 rounded-lg relative w-full flex items-center" v-if="!(typeof target === 'object' && (target[identifier].hasOwnProperty('commands') || target[identifier].hasOwnProperty('searchMode') || target[identifier].hasOwnProperty('minecraftCommand')) || target[identifier].hasOwnProperty('botStatusType') || target[identifier].hasOwnProperty('rank'))">
      <p style="min-width: 220px;" class="mr-5 relative" v-if="!Array.isArray(target)" @mouseenter="showTooltip(identifier, useEditor().getConfig.comments[useEditor().currentSection + '.' + identifier])" @mouseleave="useAppState().setHoverTooltip(false)">
        {{ headerToDisplay(props.identifier) }}
      </p>

      <!-- ============= Text Input ============ -->
      <input
        type="text"
        :readonly="props.identifier === 'configVersion'"
        v-if="typeof value === 'string' && identifier !== 'channel' && identifier !== 'playerAvatarType' && identifier !== 'botStatusType' && identifier !== 'type' && identifier !== 'advancementMessages' && !(identifier == 'deathMessages' && useEditor().getConfig.config.general.configVersion > 26)"
        v-model="target[identifier]"
        class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
      />

      <!-- ============ Boolean ============= -->
      <label class="inline-flex items-center cursor-pointer" v-if="typeof value == 'boolean'">
        <input type="checkbox" value="" class="sr-only peer" v-model="target[identifier]">
        <div class="relative w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
      </label>

      <!-- ========== Number ============= -->
      <input
        type="number"
        :disabled="props.identifier === 'configVersion' || props.identifier === 'version'"
        v-if="typeof value === 'number'"
        v-model="target[identifier]"
        aria-describedby="helper-text-explanation"
        class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
      />

      <!-- =============== Simple RPC Specific  ================ -->

      <CustomSelectControl
        v-if="identifier === 'type'"
        v-model="target[identifier]"
        class="w-full"
        :options="[
          { value: 'PLAYING', label: 'Playing' },
          { value: 'STREAMING', label: 'Streaming' },
          { value: 'WATCHING', label: 'Watching' },
          { value: 'LISTENING', label: 'Listening' },
          { value: 'COMPETING', label: 'Competing'},
          { value: 'CUSTOM_STATUS', label: 'Custom' }
        ]"
      />

      <!-- =============== Buttons ================ -->
      <div v-if="typeof target === 'object' && (target[identifier].hasOwnProperty('url'))" class="grid grid-cols-2 w-full gap-2 items-center">
        <div>
          <p class="text-sm mb-1 pl-1">Button Title</p>
          <input
            type="text"
            v-model="target[identifier].label"
            class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
          />
        </div>

        <div>
          <p class="text-sm mb-1 pl-1">Button URL</p>
          <div class="flex gap-1">
            <input
              type="text"
              v-model="target[identifier].url"
              aria-describedby="helper-text-explanation"
              class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
            />

            <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('url'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
              <FontAwesomeIcon :icon="faTrash" />
            </button>
          </div>
        </div>
      </div>

      <div v-if="typeof target === 'object' && (target[identifier].hasOwnProperty('Url'))" class="grid grid-cols-2 w-full gap-2 items-center">
        <div>
          <p class="text-sm mb-1 pl-1">Button Title</p>
          <input
            type="text"
            v-model="target[identifier].Title"
            class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
          />
        </div>

        <div>
          <p class="text-sm mb-1 pl-1">Button URL</p>
          <div class="flex gap-1">
            <input
              type="text"
              v-model="target[identifier].Url"
              aria-describedby="helper-text-explanation"
              class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
            />

            <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('Url'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
              <FontAwesomeIcon :icon="faTrash" />
            </button>
          </div>
        </div>
      </div>

      <!-- =============== Variables ================ -->
      <div v-if="typeof target === 'object' && target[identifier].hasOwnProperty('value')" class="grid grid-cols-2 w-full gap-2 items-center">
        <div>
          <p class="text-sm mb-1 pl-1">Variable</p>
          <input
            type="text"
            v-model="target[identifier].name"
            class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
          />
        </div>

        <div>
          <p class="text-sm mb-1 pl-1">Value</p>
          <div class="flex gap-1">
            <input
              type="text"
              v-model="target[identifier].value"
              aria-describedby="helper-text-explanation"
              class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
            />

            <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('value'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
              <FontAwesomeIcon :icon="faTrash" />
            </button>
          </div>
        </div>
      </div>

      <div v-if="typeof target === 'object' && target[identifier].hasOwnProperty('Value')" class="grid grid-cols-2 w-full gap-2 items-center">
        <div>
          <p class="text-sm mb-1 pl-1">Variable</p>
          <input
            type="text"
            v-model="target[identifier].Name"
            class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
          />
        </div>

        <div>
          <p class="text-sm mb-1 pl-1">Value</p>
          <div class="flex gap-1">
            <input
              type="text"
              v-model="target[identifier].Value"
              aria-describedby="helper-text-explanation"
              class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
            />

            <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('Value'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
              <FontAwesomeIcon :icon="faTrash" />
            </button>
          </div>
        </div>
      </div>

      <!-- =============== Simple Discord Link Specific =================== -->
      <CustomSelectControl
        v-if="(identifier === 'advancementMessages' || identifier == 'deathMessages') && useEditor().getConfig.config.general.configVersion > 26"
        v-model="target[identifier]"
        class="w-full"
        :options="[
          { value: 'ALWAYS', label: 'Always' },
          { value: 'NEVER', label: 'Never' },
          { value: 'GAMERULE', label: 'Game Rule' }
        ]"
      />

      <CustomSelectControl
        v-if="identifier === 'channel'"
        v-model="target[identifier]"
        class="w-full"
        :options="[
          { value: 'CHAT', label: 'Chat' },
          { value: 'EVENT', label: 'Events' },
          { value: 'CONSOLE', label: 'Console' },
          { value: 'OVERRIDE', label: 'Override' }
        ]"
      />

      <CustomSelectControl
        v-if="identifier === 'botStatusType'"
        v-model="target[identifier]"
        class="w-full"
        :options="[
          { value: 'PLAYING', label: 'Playing' },
          { value: 'STREAMING', label: 'Streaming' },
          { value: 'WATCHING', label: 'Watching' },
          { value: 'LISTENING', label: 'Listening' },
          { value: 'CUSTOM_STATUS', label: 'Custom' }
        ]"
      />

      <CustomSelectControl
        v-if="identifier === 'playerAvatarType'"
        v-model="target[identifier]"
        class="w-full"
        :options="[
          { value: 'AVATAR', label: 'Avatar' },
          { value: 'HEAD', label: 'Player Head' },
          { value: 'BODY', label: 'Body' },
          { value: 'COMBO', label: 'Combo' },
          { value: 'CUSTOM', label: 'Custom'}
        ]"
      />

      <button v-if="Array.isArray(target) && !(typeof target === 'object' && (target[identifier].hasOwnProperty('commands') || target[identifier].hasOwnProperty('largeImageKey') || target[identifier].hasOwnProperty('url') || target[identifier].hasOwnProperty('Url')) || target[identifier].hasOwnProperty('value') || target[identifier].hasOwnProperty('Value'))" @click="deleteArrayEntry(target, identifier)" type="button" class="ml-2 focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
        <FontAwesomeIcon :icon="faTrash" />
      </button>
    </div>

    <div v-if="typeof target === 'object' && target[identifier].hasOwnProperty('commands')" class="grid grid-cols-3 w-full gap-2 items-center">
      <div>
        <p class="text-sm mb-1 pl-1">Role</p>
        <input
          type="text"
          v-model="target[identifier].role"
          class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
        />
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Commands (Press enter to save)</p>
          <v-select
            class="min-w-64 textt w-full"
            v-model="target[identifier].commands"
            multiple
            taggable
            :dropdown-should-open="noOpen"
            push-tags
          ></v-select>
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Permission Level</p>
        <div class="flex gap-1">
          <input
            type="number"
            v-model="target[identifier].permissionLevel"
            aria-describedby="helper-text-explanation"
            max="4"
            min="1"
            @change="limitLevel"
            class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
          />

          <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('commands'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
            <FontAwesomeIcon :icon="faTrash" />
          </button>
        </div>
      </div>
    </div>

    <div v-if="typeof target === 'object' && target[identifier].hasOwnProperty('rank')" class="grid grid-cols-3 w-full gap-2 items-center">
      <div>
        <p class="text-sm mb-1 pl-1">Rank/Group</p>
        <input
          type="text"
          v-model="target[identifier].rank"
          class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
        />
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Discord Role</p>
        <input
          type="text"
          v-model="target[identifier].role"
          class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
        />
      </div>

      <div>
        <p></p>
        <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('rank'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
          <FontAwesomeIcon :icon="faTrash" />
        </button>
      </div>
    </div>

    <div v-if="typeof target === 'object' && target[identifier].hasOwnProperty('minecraftCommand')" class="grid grid-cols-3 w-full gap-2 items-center">
      <div>
        <p class="text-sm mb-1 pl-1">Role</p>
        <input
          type="text"
          v-model="target[identifier].discordRole"
          class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
        />
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Commands (Press enter to save)</p>
        <div class="flex gap-1">
          <v-select
            class="min-w-64 textt w-full"
            v-model="target[identifier].minecraftCommand"
            multiple
            taggable
            :dropdown-should-open="noOpen"
            push-tags
          ></v-select>
          <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('minecraftCommand'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
            <FontAwesomeIcon :icon="faTrash" />
          </button>
        </div>
      </div>
    </div>

    <div v-if="typeof target === 'object' && target[identifier].hasOwnProperty('botStatusType')" class="grid grid-cols-3 w-full gap-2 items-center">
      <div>
        <p class="text-sm mb-1 pl-1">Status</p>
        <input
          type="text"
          v-model="target[identifier].status"
          class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
        />
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Status Type</p>
        <CustomSelectControl
          v-model="target[identifier].botStatusType"
          class="w-full"
          :options="[
          { value: 'PLAYING', label: 'Playing' },
          { value: 'STREAMING', label: 'Streaming' },
          { value: 'WATCHING', label: 'Watching' },
          { value: 'LISTENING', label: 'Listening' },
          { value: 'CUSTOM_STATUS', label: 'Custom' }
        ]"
        />
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Bot Status Streaming URL</p>
        <div class="flex gap-1">
          <input
            type="text"
            v-model="target[identifier].botStatusStreamingURL"
            class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
          />

          <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('botStatusStreamingURL'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
            <FontAwesomeIcon :icon="faTrash" />
          </button>
        </div>
      </div>
    </div>

    <!-- ======================= Message Filtering =================== -->

    <div v-if="typeof target === 'object' && target[identifier].hasOwnProperty('searchMode')" class="grid grid-cols-4 w-full gap-2 items-center bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
      <div>
        <p class="text-sm mb-1 pl-1">Search</p>
        <input
          type="text"
          v-model="target[identifier].search"
          class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
        />
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Replace</p>
        <input
          type="text"
          v-model="target[identifier].replace"
          class="border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
        />
      </div>

      <div v-if="useEditor().getConfig.config.general.configVersion > 26">
        <p class="text-sm mb-1 pl-1">Target Mode</p>
        <CustomSelectControl
          v-model="target[identifier].target"
          class="w-full"
          :options="[
            { value: 'USERNAME', label: 'Username' },
            { value: 'CHAT', label: 'Chat' },
            { value: 'BOTH', label: 'Both' }
        ]"
        />
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Search Mode</p>
        <CustomSelectControl
          v-model="target[identifier].searchMode"
          class="w-full"
          :options="useEditor().getConfig.config.general.configVersion > 26 ? [
            { value: 'CONTAINS', label: 'Contains' },
            { value: 'STARTS_WITH', label: 'Starts With' },
            { value: 'MATCHES', label: 'Exact Match' },
            { value: 'REGEX', label: 'Regex' }
        ] : [
            { value: 'CONTAINS', label: 'Contains' },
            { value: 'STARTS_WITH', label: 'Starts With' },
            { value: 'MATCHES', label: 'Exact Match' },
        ]"
        />
      </div>

      <div v-if="useEditor().getConfig.config.general.configVersion > 26">
        <p class="text-sm mb-1 pl-1">Applies To</p>
        <CustomSelectControl
          v-model="target[identifier].searchMode"
          class="w-full"
          :options="[
              { value: 'DISCORD', label: 'Discord' },
              { value: 'MINECRAFT', label: 'Minecraft' },
          ]"
        />
      </div>

      <div v-if="useEditor().getConfig.config.general.configVersion > 26">
        <p class="text-sm mb-1 pl-1">Ignore Console</p>
        <label class="inline-flex items-center cursor-pointer">
          <input type="checkbox" value="" class="sr-only peer" v-model="target[identifier].ignoreConsole">
          <div class="relative w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
        </label>
      </div>

      <div>
        <p class="text-sm mb-1 pl-1">Action</p>
        <div class="flex gap-1">
          <CustomSelectControl
            v-model="target[identifier].action"
            class="w-full"
            :options="[
              { value: 'REPLACE', label: 'Replace' },
              { value: 'IGNORE', label: 'Ignore Message' }
          ]"
          />

          <button v-if="Array.isArray(target) && (typeof target === 'object' && target[identifier].hasOwnProperty('searchMode'))" @click="deleteArrayEntry(target, identifier)" type="button" class="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
            <FontAwesomeIcon :icon="faTrash" />
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import {initFlowbite} from "flowbite";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {faTrash} from "@fortawesome/free-solid-svg-icons";
import { headerToDisplay } from '@/composables/FieldUtils'
import CustomSelectControl from '@/components/editor/CustomSelectControl.vue'
import { useEditor } from '@/stores/editor'
import { useAppState } from '@/stores/appstate'

const props = defineProps(['identifier', 'value', 'target']);
initFlowbite();

const showTooltip = (title: any, data: any) => {
  if (data) {
    useAppState().setTooltipData({
      title: title,
      body: data
    })
    useAppState().setHoverTooltip(true);
  }
}

const limitLevel = () => {
    if (props.target[props.identifier].permissionLevel > 4) {
      props.target[props.identifier].permissionLevel = 4;
    }

    if (props.target[props.identifier].permissionLevel < 1) {
      props.target[props.identifier].permissionLevel = 1;
    }
}

const deleteArrayEntry = (target: any, index: any) => {
    target.splice(index, 1);
}

const noOpen = () => {
  return false;
}
</script>