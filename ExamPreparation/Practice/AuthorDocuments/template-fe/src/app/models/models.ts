export interface Author {
  id: number;
  name: string;
}

export interface Document {
  id: number;
  name: string;
  content: string;
  authors?: Author[];
}

export interface Movie {
  id: number;
  title: string;
  duration: number;
  authors?: Author[];
}

export interface DocumentDTO {
  id?: number;
  name: string;
  content: string;
  authors: string[];
}

export interface LoginRequest {
  username: string;
  password: string;
}
