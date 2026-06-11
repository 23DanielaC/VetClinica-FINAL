<template>
  <div
    class="bg-white rounded-2xl border border-slate-100 p-5
           shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.04)]
           hover:shadow-[0_4px_20px_rgba(0,0,0,0.08)] transition-all duration-300 h-full"
  >
    <div class="flex items-start justify-between gap-3">
      <div :class="['w-11 h-11 rounded-xl flex items-center justify-center bg-gradient-to-br shadow-md', gradient]">
        <component :is="icon" class="w-5 h-5 text-white" />
      </div>
      <span v-if="badge" :class="badgeClass" class="text-xs font-semibold px-2 py-0.5 rounded-full">{{ badge }}</span>
    </div>
    <p class="text-2xl font-extrabold text-slate-900 mt-4 tabular-nums tracking-tight">{{ value }}</p>
    <p class="text-sm font-medium text-slate-500 mt-1">{{ label }}</p>
    <p v-if="hint" class="text-xs text-slate-400 mt-2">{{ hint }}</p>
  </div>
</template>

<script setup>
import {
  BanknotesIcon, ShieldExclamationIcon, BeakerIcon, ClipboardDocumentCheckIcon,
} from '@heroicons/vue/24/outline'
import { computed } from 'vue'

const props = defineProps({
  label: String,
  value: [String, Number],
  hint: String,
  badge: String,
  badgeClass: { type: String, default: 'bg-slate-100 text-slate-600' },
  iconName: { type: String, default: 'money' },
  gradient: { type: String, default: 'from-emerald-500 to-teal-400' },
})

const icons = {
  money: BanknotesIcon,
  vaccine: ShieldExclamationIcon,
  medicine: BeakerIcon,
  consult: ClipboardDocumentCheckIcon,
}

const icon = computed(() => icons[props.iconName] ?? BanknotesIcon)
</script>
