<template>
  <transition
      enter-active-class="animate__animated animate__bounceIn"
      leave-active-class="animate__animated animate__bounceOut"
      class="fixed toasttt"
      :class="getToastClass()"
  >
    <div
        class="flex items-center w-full max-w-xs p-4 mb-4 text-gray-500 bg-ct-card-light rounded-lg shadow dark:bg-ct-card-dark dark:text-gray-400"
        role="alert"
        v-if="toast !== undefined"
    >
      <SuccessIcon v-if="toast.type === 'success'" />
      <InfoIcon v-if="toast.type === 'info'" />
      <WarningIcon v-if="toast.type === 'warning'" />
      <ErrorIcon v-if="toast.type === 'error'" />

      <div class="ms-3 text-sm font-normal dark:text-white">{{ toast.title }}</div>

      <CloseButton />
    </div>
  </transition>
</template>

<script setup lang="ts">
import { useToast } from '@/stores/toaststore'
import SuccessIcon from '@/components/toast/icons/SuccessIcon.vue'
import { computed } from 'vue'
import InfoIcon from '@/components/toast/icons/InfoIcon.vue'
import WarningIcon from '@/components/toast/icons/WarningIcon.vue'
import ErrorIcon from '@/components/toast/icons/ErrorIcon.vue'
import CloseButton from '@/components/toast/icons/CloseButton.vue'

const toast = computed(() => {
  return useToast().getActiveToast
})

function getToastClass() {
  if (toast.value) {
    switch (toast.value.position) {
      case 'top-left':
        return 'left-5 top 20'
      case 'top-right':
        return 'right-5 top-20'
      case 'bottom-left':
        return 'left-5 bottom-20'
      case 'bottom-right':
        return 'right-5 bottom-20'
    }
  }

  return 'right-5 top-20'
}
</script>