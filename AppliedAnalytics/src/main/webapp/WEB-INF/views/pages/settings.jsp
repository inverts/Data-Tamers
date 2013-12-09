<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h3>Settings</h3>
<div class="settings">
	<div class="left-pane">
		<c:choose>
			<c:when test="${not empty settings.getGoogleUserData().getPicture()}" >
				<img class="profile-image-large" title="" src="${settings.getGoogleUserData().getPicture()}?sz=150" /><br />
			</c:when>
			<c:otherwise>
				<img class="profile-image-large" src="<c:url value="/cache/images/default_user_90.png" />" /><br />
			</c:otherwise>
		</c:choose>
	</div>
	<table>
		<tbody>
			<tr><td class="rowlabel">Name</td><td>${ settings.getGoogleUserData().getName() }</td></tr>
			<tr><td class="rowlabel">Email</td><td>${ settings.getGoogleUserData().getEmail() }</td></tr>
			<tr><td class="rowlabel">Account</td><td>
			<c:choose>
				<c:when test="${not empty settings.getCurrentAccounts() && not empty settings.getCurrentAccounts().getItems()}" >
					<select id="select-account">
					<c:forEach var="account" items="${ settings.getCurrentAccounts().getItems() }">
						<option value="${ account.getId() }" ${settings.getAccountSelection() == account ? "selected" : "" }>
						${ account.getName() }
						</option>
					</c:forEach>
					</select>
				</c:when>
				<c:otherwise>
					(Account List Unavailable)
				</c:otherwise>
			</c:choose>
			</td></tr>
			
			<c:choose>
				<c:when test="${not empty settings.getCurrentWebproperties()}" >
					<tr><td class="rowlabel">Property</td><td>
					<c:choose>
						<c:when test="${not empty settings.getCurrentWebproperties().getItems()}" >
							<select id="select-property">
							<c:forEach var="property" items="${ settings.getCurrentWebproperties().getItems() }">
								<option value="${ property.getId() }" ${settings.getPropertySelection() == property ? "selected" : "" }>
								${ property.getName() }
								</option>
							</c:forEach>
							</select>
						</c:when>
						<c:otherwise>
							(Property List Unavailable)
						</c:otherwise>
					</c:choose>
					</td></tr>
				</c:when>
			</c:choose>
			
			<c:choose>
				<c:when test="${not empty settings.getCurrentProfiles()}" >
					<tr><td class="rowlabel">Profile</td><td>
					<c:choose>
						<c:when test="${not empty settings.getCurrentProfiles().getItems()}" >
							<select id="select-profile">
							<c:forEach var="profile" items="${ settings.getCurrentProfiles().getItems() }">
								<option value="${ profile.getId() }" ${settings.getProfileSelection() == profile ? "selected" : "" }>
								${ profile.getName() }
								</option>
							</c:forEach>
							</select>
						</c:when>
						<c:otherwise>
							(Profile List Unavailable)
						</c:otherwise>
					</c:choose>
					</td></tr>
				</c:when>
			</c:choose>
			
			<c:choose>
				<c:when test="${not empty settings.getCurrentProfiles()}" >
					<tr><td class="rowlabel"></td><td><input id="update-button" type="button" value="Update" /></td></tr>
				</c:when>
			</c:choose>
			
		</tbody>
	</table>
	
</div>
<script type="text/javascript">
$('#select-account').change( function () {
	document.location.href = "?account=" + $('#select-account option:selected').val();
})
$('#select-property').change( function () {
	document.location.href = "?property=" + $('#select-property option:selected').val();
})
$('#select-profile').change( function () {
	document.location.href = "?profile=" + $('#select-profile option:selected').val();
})
$('#update-button').click( function () {
	document.location.href = "?update=1";
})
</script>
