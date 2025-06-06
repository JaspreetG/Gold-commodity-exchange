
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ChevronDown } from "lucide-react";

interface Country {
  code: string;
  name: string;
  flag: string;
  dialCode: string;
}

const countries: Country[] = [
  { code: "US", name: "United States", flag: "🇺🇸", dialCode: "+1" },
  { code: "CA", name: "Canada", flag: "🇨🇦", dialCode: "+1" },
  { code: "GB", name: "United Kingdom", flag: "🇬🇧", dialCode: "+44" },
  { code: "DE", name: "Germany", flag: "🇩🇪", dialCode: "+49" },
  { code: "FR", name: "France", flag: "🇫🇷", dialCode: "+33" },
  { code: "JP", name: "Japan", flag: "🇯🇵", dialCode: "+81" },
  { code: "IN", name: "India", flag: "🇮🇳", dialCode: "+91" },
  { code: "AU", name: "Australia", flag: "🇦🇺", dialCode: "+61" },
];

interface CountryCodeSelectProps {
  selectedCountry: Country;
  onSelect: (country: Country) => void;
}

const CountryCodeSelect = ({ selectedCountry, onSelect }: CountryCodeSelectProps) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="relative">
      <Button
        type="button"
        variant="outline"
        onClick={() => setIsOpen(!isOpen)}
        className="w-20 h-10 px-2 border-gray-200 bg-white hover:bg-gray-50 font-light flex items-center justify-between"
      >
        <div className="flex items-center space-x-1">
          <span className="text-sm">{selectedCountry.flag}</span>
          <span className="text-xs">{selectedCountry.dialCode}</span>
        </div>
        <ChevronDown className="h-3 w-3" />
      </Button>
      
      {isOpen && (
        <div className="absolute top-full left-0 mt-1 w-64 bg-white border border-gray-200 rounded-md shadow-lg z-50 max-h-60 overflow-auto">
          {countries.map((country) => (
            <button
              key={country.code}
              type="button"
              onClick={() => {
                onSelect(country);
                setIsOpen(false);
              }}
              className="w-full text-left px-3 py-2 hover:bg-gray-50 flex items-center space-x-3 font-light"
            >
              <span className="text-lg">{country.flag}</span>
              <span className="text-sm text-gray-600">{country.dialCode}</span>
              <span className="text-sm text-black">{country.name}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

export default CountryCodeSelect;
export type { Country };
