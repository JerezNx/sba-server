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
  <div class="task-log">
    <iframe src="/static/task-log.html" style="width: 100%;height: 100%"></iframe>
<!--    <iframe src="https://console.suxiniot.com/weld/bulletin-board/"></iframe>-->
<!--    <el-table
        :data="tableData"
        border
        style="width: 100%">
      <el-table-column
          prop="date"
          label="日期"
          width="180">
      </el-table-column>
      <el-table-column
          prop="name"
          label="姓名"
          width="180">
      </el-table-column>
      <el-table-column
          prop="address"
          label="地址">
      </el-table-column>
    </el-table>-->
  </div>
</template>

<script>

export default {
  components: {
  },
  props: {
    instance: { //<1>
      type: Object,
      required: true
    }
  },
  data: () => ({
    taskLogs: {},
    condition: {},
    pageArgs: {
      total: 0,
      pageNum: 1,
      pageSize: 13
    },
    tableData: [{
      date: '2016-05-02',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1518 弄'
    }, {
      date: '2016-05-04',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1517 弄'
    }, {
      date: '2016-05-01',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1519 弄'
    }, {
      date: '2016-05-03',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1516 弄'
    }]
  }),
  async created() {
    // await this.getThreadPool();
    // setInterval(this.getThreadPool, 1000)
  },
  mounted() {
  },
  methods: {
    async getThreadPool() {
      const params = {
        // args: JSON.parse(JSON.stringify(this.condition)),
        pageNum: this.pageArgs.pageNum,
        pageSize: this.pageArgs.pageSize
      };
      const token = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaW5mbyI6eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJyb2xlSWQiOjEsImdyb3VwSWQiOjEsImZpcm1JZCI6MSwiZmlybSI6bnVsbCwiZGVwYXJ0bWVudElkIjozNywiZGVwYXJ0bWVudENvZGUiOiIxLTM3LSIsImRlcGFydG1lbnROYW1lIjoi5LqR5bmz5Y-wIiwiYXV0aG9yaXR5Q29kZXMiOm51bGwsIm9wZW5JZCI6bnVsbCwidGltZXN0YW1wIjowLCJkZXYiOnRydWUsIm9wcyI6ZmFsc2UsImZpcm1NYW5hZ2VyIjpmYWxzZSwic2lrc2hyIjpmYWxzZSwicGxhdGZvcm1NYW5hZ2VyIjpmYWxzZX0sImlhdCI6MTY3NzQ2NTQ5NCwiZXhwIjoxNzcyMDczNDk0fQ.bHOTmelaRujo37Mqv5oK6MEWpE_NZAN-fJ08lNAgSQzPFOwcw4eHEXat33CYvPRm3O24_mO8HoS46zGxrPNBog';
      const response = await this.instance.axios.get('actuator/taskLog', {params},  {
        headers: {
          'Authorization': token
        }
      }); //<2>
      this.taskLogs = response.data;
    }
  },
};
</script>

<style>
.task-log {
  //padding: 10px;
  padding: 3rem 1.5rem;
  width: 100%;
  height: 100%;
}
</style>
