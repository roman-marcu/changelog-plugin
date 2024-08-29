<#ftl output_format="plainText" >
## [Version ${projectVersion}]

<#list commits>
  <ul>
    <#items as commit>
      <li>
        <b>${commit.type}</b> : ${commit.description}
      </li>
    </#items>
  </ul>
</#list>

