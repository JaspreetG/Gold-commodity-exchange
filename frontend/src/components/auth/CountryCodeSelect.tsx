import { useEffect, useRef, useState } from "react";
import { Button } from "@/components/ui/button";
import { ChevronDown } from "lucide-react";
import Flag from "react-world-flags";

interface Country {
  code: string;
  name: string;
  dialCode: string;
}

const countries: Country[] = [
  { code: "IN", name: "India", dialCode: "+91" },
  { code: "US", name: "United States", dialCode: "+1" },
  { code: "CN", name: "China", dialCode: "+86" },
  { code: "JP", name: "Japan", dialCode: "+81" },
  { code: "DE", name: "Germany", dialCode: "+49" },
  { code: "GB", name: "United Kingdom", dialCode: "+44" },
  { code: "FR", name: "France", dialCode: "+33" },
  { code: "BR", name: "Brazil", dialCode: "+55" },
  { code: "IT", name: "Italy", dialCode: "+39" },
  { code: "CA", name: "Canada", dialCode: "+1" },
  { code: "RU", name: "Russia", dialCode: "+7" },
  { code: "KR", name: "South Korea", dialCode: "+82" },
  { code: "AU", name: "Australia", dialCode: "+61" },
  { code: "ES", name: "Spain", dialCode: "+34" },
  { code: "MX", name: "Mexico", dialCode: "+52" },
  { code: "ID", name: "Indonesia", dialCode: "+62" },
  { code: "SA", name: "Saudi Arabia", dialCode: "+966" },
  { code: "TR", name: "Turkey", dialCode: "+90" },
  { code: "ZA", name: "South Africa", dialCode: "+27" },
  { code: "AR", name: "Argentina", dialCode: "+54" },
  { code: "SE", name: "Sweden", dialCode: "+46" },
  { code: "CH", name: "Switzerland", dialCode: "+41" },
  { code: "NL", name: "Netherlands", dialCode: "+31" },
  { code: "SG", name: "Singapore", dialCode: "+65" },
  { code: "AE", name: "United Arab Emirates", dialCode: "+971" },
];

interface CountryCodeSelectProps {
  selectedCountry: Country;
  onSelect: (country: Country) => void;
}

const CountryCodeSelect = ({
  selectedCountry,
  onSelect,
}: CountryCodeSelectProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  // Detect outside click
  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target as Node)
      ) {
        setIsOpen(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);
  return (
    <div ref={dropdownRef} className="relative">
      <Button
        type="button"
        variant="outline"
        onClick={() => setIsOpen(!isOpen)}
        className="w-24 h-10 px-2 border-gray-200 bg-white hover:bg-gray-50 font-light flex items-center justify-between"
      >
        <div className="flex items-center space-x-1">
          <Flag
            code={selectedCountry.code}
            style={{ width: 20, height: 14, borderRadius: 2 }}
          />
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
              <Flag
                code={country.code}
                style={{ width: 20, height: 14, borderRadius: 2 }}
              />
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
