<template>
  <span class="ivu-typography">
    <code>
      <slot></slot>
    </code>
  </span>
</template>
<script>
export default {
  name: "MyText"
}
</script>
<style scoped>
.ivu-typography {
  color: #515a6e;
  overflow-wrap: break-word;
}

.ivu-typography code {
  margin: 0 0.2em;
  padding: 0.2em 0.4em 0.1em;
  font-size: 85%;
  background: rgba(150, 150, 150, .1);
  border: 1px solid rgba(100, 100, 100, .2);
  border-radius: 3px;
}
code, kbd, pre, samp {
  font-family: Consolas,Menlo,Courier,monospace;
  color: #515a6e;
}
</style>
