<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>boss测试</title>
		<script type="text/javascript">
			function formCheck()
			{
				//请求地址
				var url = document.getElementById("url").value;
				//请求报文
				var req = document.getElementById("req").value;
				
				if (url == '' || url == undefined || url == null) {
					alert("请输入地址");
					return;
				}
				
				if (req == '' || req == undefined || req == null) {
					alert("请输入请求报文");
					return;
				}
       
	            document.form1.submit();
			}
		</script>
	</head>
	<body>
		<form name="form1" method="post" action="bossdeal.do">
			<table width="600">
				<tr>
					<td width="10%">地址：</td>
					<td><input type="text" name="url" id="url" size="150" maxlength="100" value="${result.url }" /></td>
					<td><input type="button" value="提交" onclick="formCheck();" /></td>
				</tr>
				<tr>
					<td style="font-size: 12px">
						请求报文
						http://10.32.205.98:9110/fcgi-bin/NetB_nj
					</td>
					<td>
						<textarea name="req" id="req" rows="30" cols="135">${result.req }</textarea>
					</td>
				</tr>
				<tr>
					<td style="font-size: 12px">
						响应报文
					</td>
					<td>
						<textarea id="rsp" name="rsp" rows="30" cols="135">${result.rsp }</textarea>
					</td>
				</tr>
			</table>

		</form>
	</body>
</html>
