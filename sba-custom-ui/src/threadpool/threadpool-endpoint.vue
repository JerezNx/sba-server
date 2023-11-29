<!--
  - Copyright 2014-2018 the original author or authors.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <div class="thread-pool">
    <el-row :gutter="20">
      <el-col :span="8" v-for="(threadPool, index) in threadPools" :key="index">
        <ThreadpoolCard :thread-pool="threadPool"></ThreadpoolCard>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ThreadpoolCard from "@/threadpool/threadpool-card.vue";

export default {
  components: {
    ThreadpoolCard,
  },
  props: {
    instance: { //<1>
      type: Object,
      required: true
    }
  },
  data: () => ({
    threadPools: {}
  }),
  async created() {
    await this.getThreadPool();
    setInterval(this.getThreadPool, 1000)
  },
  mounted() {
  },
  methods: {
    async getThreadPool() {
      const response = await this.instance.axios.get('actuator/threadPool'); //<2>
      this.threadPools = response.data;
    }
  },
};
</script>

<style>
.thread-pool {
  //padding: 10px;
  padding: 3rem 1.5rem;
}
</style>
