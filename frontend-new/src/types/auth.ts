
export interface User {
  id: string;
  phone: string;
  name: string;
  balances: {
    usd: number;
    gold: number;
  };
}

export interface PhoneCredentials {
  phone: string;
}

export interface TOTPCredentials {
  phone: string;
  totpCode: string;
}

export interface RegistrationData {
  phone: string;
  qrCode: string;
  secret: string;
}
