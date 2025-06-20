export interface Feedback {
  id: number;
  message: string;
  timestamp: string;
  customer: Customer;
}

export interface AddFeedbackResponse {
  isNaughty: boolean;
  highlightedMessage?: string;
}

export interface Customer{
    id: number;
    name: string;
    email: string;
}


