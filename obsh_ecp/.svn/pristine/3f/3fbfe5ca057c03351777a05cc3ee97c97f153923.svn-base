<%@page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fn" prefix="fn"%>
<%@ page isELIgnored="false"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>缓存列表</title>
		<script type="text/javascript">
			function delMemcach(id ,sql)
{
       
          document.form1.action="memcach.do?method=deleteMemcach&id="+id+"&sql="+sql;
          document.form1.submit();
          document.close();
	}
		</script>
	</head>
	<body>
		<form id="form1" name="form1" method="post" action="">
			<table width="600">
				<tr>
					<td style="font-size: 12px">
						缓存代号
					</td>
					<td style="font-size: 12px">
						缓存名称
					</td>
					<td style="font-size: 12px">
						清空缓存
					</td>
				</tr>
				<c:forEach var="cachList" items="${cach}">
					<tr>
						<td style="font-size: 12px">
							${fn:escapeXml(cachList.f_cache_prefix)}
						</td>
						<td style="font-size: 12px">
							${fn:escapeXml(cachList.f_cache_name)}
						</td>
						<td style="font-size: 12px">
							<input type="button" value="清空缓存" onClick='delMemcach("${fn:escapeXml(cachList.f_cache_prefix)}","${fn:escapeXml(cachList.f_cache_sufix_sql)}")' />
						</td>
					</tr>
				</c:forEach>
			</table>

		</form>
	</body>
</html>