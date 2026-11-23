<script setup>
import { ref, onMounted } from 'vue'

const API = 'http://localhost:8080/api'
const items = ref([])

async function loadItems() {
  const response = await fetch(API + '/items')
  items.value = await response.json()
}

// se apeleaza automat cand se deschide pagina
onMounted(loadItems)
</script>

<template>
  <div class="page">
    <h1>WardrobeAI</h1>
    <h2>Garderoba mea</h2>

    <div class="grid">
      <div v-for="item in items" :key="item.id" class="card">
        <h3>{{ item.name }}</h3>
        <p>{{ item.brand }} · {{ item.color }}</p>
        <p v-if="item.category">{{ item.category.name }}</p>
        <p>Purtata de {{ item.timesWorn }} ori</p>
      </div>
    </div>
  </div>
</template>

<style>
body {
  margin: 0;
  font-family: sans-serif;
  background: #faf7f5;
}
.page {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px;
}
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}
.card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}
</style>