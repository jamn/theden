<form class="login-box">
	<label for="email">Email:</label>
	<input type="text" name="email" id="email" style="font-size:1em;padding:4px;margin:4px;" value="${client?.email}" autofocus="autofocus">
	<input type="text" name="email2" id="email2" style="display:none;">

	<label for="password">Password:</label>
	<input type="password" name="password" id="password" style="font-size:1em;padding:4px;margin:4px;" value="${client?.password}">

	<label for="first-name" class="new-user">First Name:</label>
	<input type="text" name="first-name" id="firstName" class="new-user" style="font-size:1em;padding:4px;margin:4px;">

	<label for="last-name" class="new-user">Last Name:</label>
	<input type="text" name="last-name" id="lastName" class="new-user" style="font-size:1em;padding:4px;margin:4px;"><br>

	<label for="phone" class="new-user">Phone #:</label>
	<input type="text" name="phone" id="phoneNumber" class="new-user" style="font-size:1em;padding:4px;margin:4px;"><br>

	<div id="registerLink">New Client?</div>
</form>
<div id="loginButton"><div class="label">Login</div><img width='20px' height'20px' src="${resource(dir:'images', file:'spinner.gif')}" style="display:none;" class="spinner"></div>