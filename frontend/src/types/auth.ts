export interface User {
  userName: string;
  phoneNumber: string;
  balances: {
    inr: number;
    gold: number;
  };
}

export interface PhoneCredentials {
  phoneNumber: string;
}

export interface TOTPCredentials {
  phoneNumber: string;
  totpCode: string;
}

export interface RegistrationData {
  phoneNumber: string;
  userName: string;
}
