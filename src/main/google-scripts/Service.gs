function test() {
  Logger.log(generateIdentifier('NamespaceTest'));
}

function generateIdentifier(namespace) {
  const sheet = SpreadsheetApp.getActiveSpreadsheet().getActiveSheet();

  try {
    const lock = LockService.getScriptLock();
    const result = getNamespaceRow(namespace);

    if (result.row === -1) {
      sheet.appendRow([namespace, 1]);
      return namespace + '-1';
    } else {
      const newValue = result.value + 1;
      sheet.getRange(result.row, 2).setValue(newValue);
      return namespace + '-' + newValue;
    }

  } finally {
    lock.releaseLock();
  }

}

function getNamespaceRow(name) {
  const sheet = SpreadsheetApp.getActiveSpreadsheet().getActiveSheet();
  const rowsCount = sheet.getLastRow() - 1;
  const data = sheet.getRange(2, 1, rowsCount, sheet.getLastColumn()).getValues();

  for (var i=0; i<data.length; i++) {
    var row = data[i];
    if (name === row[0]) {
      return { row: i+2, name: name, value: row[1] };
    }
  }

  return { row: -1, name: null, value: null };
}

