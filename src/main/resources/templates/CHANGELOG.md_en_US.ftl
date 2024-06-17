<#ftl output_format="plainText" >
<#assign aDateTime = .now>
<#assign aDate = aDateTime?date>

## [Version 1.1.0] (${aDate?iso_utc})
<#list commits>
  <ul>
    <#items as commit>
      <li>
        <b>${commit.type}</b> : ${commit.description}
      </li>
    </#items>
  </ul>
</#list>