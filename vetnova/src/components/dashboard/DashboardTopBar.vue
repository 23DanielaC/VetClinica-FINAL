<template>
  <div class="flex flex-col gap-4 xl:flex-row xl:items-center xl:justify-between bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)] px-5 py-3.5">
    <div class="relative flex-1 max-w-md">
      <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
      <input
        v-model="query"
        type="search"
        placeholder="Buscar clientes, mascotas, turnos..."
        class="w-full pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200/80 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/25 focus:border-primary/40 transition"
      />
    </div>

    <div class="flex items-center gap-2 sm:gap-3">
      <button type="button" class="relative p-2.5 rounded-xl bg-slate-50 border border-slate-100 hover:bg-slate-100 transition-colors">
        <BellIcon class="w-5 h-5 text-slate-600" />
        <span class="absolute top-2 right-2 w-2 h-2 bg-rose-500 rounded-full ring-2 ring-white" />
      </button>
      <div class="flex items-center gap-2.5 pl-2 sm:pl-3 sm:border-l border-slate-100">
        <div class="text-right hidden sm:block">
          <p class="text-sm font-bold text-slate-800">{{ name }}</p>
          <p class="text-xs text-slate-400">{{ role }}</p>
        </div>
        <div class="w-10 h-10 rounded-xl bg-gradient-to-br from-primary-dark to-indigo-500 flex items-center justify-center text-white font-bold shadow-lg shadow-primary/25">
          {{ initial }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { MagnifyingGlassIcon, BellIcon } from '@heroicons/vue/24/outline'

defineProps({
  name: { type: String, default: 'Administrador' },
  role: { type: String, default: 'Admin' },
  initial: { type: String, default: 'A' },
})

const query = ref('')
</script>
