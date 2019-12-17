/**
 * ---------------------------------------------
 * HTTP Get requests
 * ---------------------------------------------
 */
function doGet(request) {

  if (request.parameter.namespaceName) {

    const id = generateIdentifier(request.parameter.namespaceName);
    return ContentService.createTextOutput(id);

  } else {

    return ContentService.createTextOutput('');

  }

}
