/* JavaScript Google API Authorization
 * 
 * JavaScript Client Library Reference:
 * https://developers.google.com/api-client-library/javascript/reference/referencedocs
 * 
 * Process:
 * Library loaded >> handleClientLoad >> checkAuth ... (async)
 * handleAuthResult >> 
 */
  var CLIENT_ID = '409721414292.apps.googleusercontent.com';
  var SCOPES = [
//      'https://www.googleapis.com/auth/analytics.readonly', // Read-only access
      'https://www.googleapis.com/auth/analytics',	// Standard access
//      'https://www.googleapis.com/auth/analytics.manage.users', // Administrative access
//      'https://www.googleapis.com/auth/plus.login', // Google+ access
    ];

  /**
   * Called when the client library is loaded.
   */
  function handleClientLoad() {
    checkAuth();
  }

  /**
   * Check if the current user has authorized the application.
   */
  function checkAuth() {
    gapi.auth.authorize(
        {'client_id': CLIENT_ID, 'scope': SCOPES.join(' '), 'immediate': true},
        handleAuthResult);
  }

  /**
   * Callback for when authorization server replies.
   *
   * @param {Object} authResult Authorization result.
   */
  function handleAuthResult(authResult) {
    if (authResult) {
      alert();
    } else {
      // No access token could be retrieved, force the authorization flow.
      gapi.auth.authorize(
          {'client_id': CLIENT_ID, 'scope': SCOPES, 'immediate': false},
          handleAuthResult);
    }
   }
  
  
  /**
   * Load the Analytics API client.
   * @param {Function} callback Function to call when the client is loaded.
   */
  function loadAnalyticsClient(callback) {
    	gapi.client.load('analytics', 'v3', callback);
  }

  
  /**
   * Load the Google+ API client.
   * @param {Function} callback Function to call when the client is loaded.
   */
  function loadPlusClient(callback) {
    	gapi.client.load('plus', 'v1', callback);
  }
  
  