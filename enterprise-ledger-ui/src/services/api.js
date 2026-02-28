import HttpClient from './http/httpClient';

const apiFetch = new HttpClient();
class AccountApi {
  getAccounts() {
    return apiFetch.get('/api/v1/accounts');
  }

  createJournal(payload, headers) {
    return apiFetch.post('/api/v1/journals', payload, headers);
  }
}
export default new AccountApi();
