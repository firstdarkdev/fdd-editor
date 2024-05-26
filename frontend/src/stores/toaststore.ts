import { defineStore } from 'pinia'

interface ToastState {
    title: string
    timeout: number
    type: 'info' | 'warning' | 'error' | 'success'
    position: 'top-left' | 'top-right' | 'bottom-left' | 'bottom-right'
}

interface ToastQueue {
    activeToast: ToastState | undefined
    toastQueue: ToastState[]
}

export const useToast = defineStore('frostbite-toast', {
    state: (): ToastQueue => ({
        activeToast: undefined,
        toastQueue: []
    }),

    actions: {
        showToast(
            title: string,
            timeout: number,
            type: ToastState['type'],
            position: ToastState['position'] = 'top-right'
        ): void {
            const toast: ToastState = {
                title: title,
                timeout: timeout,
                type: type,
                position: position
            }

            if (!this.activeToast) {
                this.activeToast = toast
                setTimeout(() => this.removeToast(), toast.timeout > 0 ? toast.timeout : 2000)
            } else {
                this.toastQueue.push(toast)
            }
        },

        removeToast() {
            this.activeToast = undefined

            if (this.toastQueue.length > 0) {
                const nextToast = this.toastQueue.shift() as ToastState
                this.activeToast = nextToast
                setTimeout(() => this.removeToast(), nextToast.timeout > 0 ? nextToast.timeout : 2000)
            }
        }
    },
    getters: {
        getActiveToast(state) {
            return state.activeToast
        }
    }
})