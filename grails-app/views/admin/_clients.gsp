<div class="row">
	<h1 class="page-header">Client Details</h1>
	<div id="addClient">
		<div class="add-client green-button">+</div>
		<div class="add-client-label">Add Client</div>
	</div>
</div>
<div class="row" id="lastNameFilters">
	<div class="col-sm-12">
		<g:each in="${filterLetters}" var="letter">
			<a class="last-name-filter" value="${letter}">${letter}</a>
		</g:each>
		<span class="spacer reset-search"> &oline; </span>
		<a class="last-name-filter reset-search reset" value="Reset">Reset Search</a>
	</div>
</div>
<div class="row">
	<div class="col-sm-4 col-sm-offset-right-8">
		<select id="clientsDetailsSelector" class="form-control" multiple>
			<g:each in="${clients}" var="client" status="i">
				<g:set var="currentLastNameStartsWith" value="${client.lastName.substring(0,1)}" />
				
				<g:if test="${(i == 0)}">
					<g:set var="previousLastNameStartedWith" value="${client.lastName.substring(0,1)}" />
					<optgroup label="${currentLastNameStartsWith}">
						<option value="${client.id}">${client.lastName}, ${client.firstName}</option>
				</g:if>
				<g:elseif test="${(i == clients.size())}">
					</optgroup>
				</g:elseif>
				<g:elseif test="${(currentLastNameStartsWith != previousLastNameStartedWith)}">
					</optgroup>
					<optgroup label="${currentLastNameStartsWith}">
						<option value="${client.id}">${client.lastName}, ${client.firstName}</option>
				</g:elseif>
				<g:else>
					<option value="${client.id}">${client.lastName}, ${client.firstName}</option>
				</g:else>

				<g:set var="previousLastNameStartedWith" value="${client.lastName.substring(0,1)}" />
			</g:each>
		</select>
	</div>
</div>
<div class="row">
	<div id="clientDetails"></div>
</div>
<div class="row">
	<div id="clientInfoForm"></div>
</div>

